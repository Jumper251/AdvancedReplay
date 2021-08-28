package me.jumper251.replay.replaysystem.recording.optimization;

public enum ReplayQuality {

    LOW("§cLow"),
    MEDIUM("§eMedium"),
    HIGH("§aHigh");

    private String qualityName;

    private ReplayQuality(String qualityName) {
        this.qualityName = qualityName;
    }

    public String getQualityName() {
        return qualityName;
    }

}
