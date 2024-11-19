package me.jumper251.replay.replaysystem.recording.optimization;

import me.jumper251.replay.filesystem.ConfigMessage;
import me.jumper251.replay.filesystem.Messages;

public enum ReplayQuality {
	
	LOW(Messages.QUALITY_LOW),
	MEDIUM(Messages.QUALITY_MEDIUM),
	HIGH(Messages.QUALITY_HIGH);
	
	private ConfigMessage qualityName;
	
	private ReplayQuality(ConfigMessage qualityName) {
		this.qualityName = qualityName;
	}
	
	public String getQualityName() {
		return qualityName.getFullMessage();
	}

}
