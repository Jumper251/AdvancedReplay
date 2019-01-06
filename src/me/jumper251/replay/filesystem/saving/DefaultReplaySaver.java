package me.jumper251.replay.filesystem.saving;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ReplayData;
import me.jumper251.replay.utils.LogUtils;

public class DefaultReplaySaver implements IReplaySaver{

	public final static File DIR = new File(ReplaySystem.getInstance().getDataFolder() + "/replays/");
	private boolean reformatting;
	
	@Override
	public void saveReplay(Replay replay) {
		
		if(!DIR.exists()) DIR.mkdirs();
		
		File file = new File(DIR, replay.getId() + ".replay");
		
		
		try {
			if (!file.exists()) file.createNewFile();

			FileOutputStream fileOut = new FileOutputStream(file);
			GZIPOutputStream gOut = new GZIPOutputStream(fileOut);
			ObjectOutputStream objectOut = new ObjectOutputStream(gOut);
			
			objectOut.writeObject(replay.getData());
			objectOut.flush();
			
			gOut.close();
			fileOut.close();
			objectOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	

	@Override
	public Replay loadReplay(String replayName) {
		try {
			
			File file = new File(DIR, replayName + ".replay");
			
			FileInputStream fileIn = new FileInputStream(file);
			GZIPInputStream gIn = new GZIPInputStream(fileIn);
			ObjectInputStream objectIn = new ObjectInputStream(gIn);
			
			ReplayData data = (ReplayData) objectIn.readObject();

			objectIn.close();
			gIn.close();
			fileIn.close();

			
			return new Replay(replayName, data);
			
		} catch (Exception e) {
			if(!reformatting) e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean replayExists(String replayName) {
		File file = new File(DIR, replayName + ".replay");
		
		return file.exists();
	}

	@Override
	public void deleteReplay(String replayName) {
		File file = new File(DIR, replayName + ".replay");
		
		if (file.exists()) file.delete();
	}
	
	public void reformatAll() {
		this.reformatting = true;
		if (DIR.exists()) {
			Arrays.asList(DIR.listFiles()).stream()
			.filter(file -> (file.isFile() && file.getName().endsWith(".replay")))
			.map(File::getName)
			.collect(Collectors.toList())
			.forEach(file -> reformat(file.replaceAll("\\.replay", "")));	
		}
		
		this.reformatting = false;
	}
	
	private void reformat(String replayName) {
		Replay old = loadReplay(replayName);
		if (old == null) {
			LogUtils.log("Reformatting: " + replayName);
			
			try {
				File file = new File(DIR, replayName + ".replay");
				
				FileInputStream fileIn = new FileInputStream(file);
				ObjectInputStream objectIn = new ObjectInputStream(fileIn);
				
				ReplayData data = (ReplayData) objectIn.readObject();

				objectIn.close();
				fileIn.close();

				deleteReplay(replayName);
				saveReplay(new Replay(replayName, data));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
	}


	@Override
	public List<String> getReplays() {
		List<String> files = new ArrayList<String>();
		
		if (DIR.exists()) {
			for (File file : Arrays.asList(DIR.listFiles())) {
				if (file.isFile() && file.getName().endsWith(".replay")) {
					files.add(file.getName().replaceAll("\\.replay", ""));
				}
			}
		}
		return files;
	}

}
