package me.jumper251.replay.filesystem.saving;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ReplayData;

public class DefaultReplaySaver implements IReplaySaver{

	public final static File dir = new File(ReplaySystem.getInstance().getDataFolder() + "/replays/");

	
	@Override
	public void saveReplay(Replay replay) {
		
		if(!dir.exists()) dir.mkdirs();
		
		File file = new File(dir, replay.getId() + ".replay");
		
		
		try {
			if (!file.exists()) file.createNewFile();

			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			
			objectOut.writeObject(replay.getData());
			objectOut.flush();
			
			fileOut.close();
			objectOut.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}

	@Override
	public Replay loadReplay(String replayName) {
		try {
			
			File file = new File(dir, replayName + ".replay");
			
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			
			ReplayData data = (ReplayData) objectIn.readObject();

			objectIn.close();
			fileIn.close();
			
			return new Replay(replayName, data);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean replayExists(String replayName) {
		File file = new File(dir, replayName + ".replay");
		
		return file.exists();
	}

	@Override
	public void deleteReplay(String replayName) {
		File file = new File(dir, replayName + ".replay");
		
		if (file.exists()) file.delete();
	}

}
