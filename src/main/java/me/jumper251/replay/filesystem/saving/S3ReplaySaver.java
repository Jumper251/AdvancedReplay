package me.jumper251.replay.filesystem.saving;

import io.minio.BucketExistsArgs;
import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import me.jumper251.replay.ReplaySystem;
import me.jumper251.replay.replaysystem.Replay;
import me.jumper251.replay.replaysystem.data.ReplayInfo;
import me.jumper251.replay.utils.fetcher.Consumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

public class S3ReplaySaver implements IReplaySaver {
    private String endpointUrl;
    private String accessKey;
    private String secretKey;
    private String bucketName;

    private Map<String, ReplayInfo> replayCache;

    private MinioClient minioClient;

    public S3ReplaySaver(String endpointUrl, String accessKey, String secretKey, String bucketName) {
        this.endpointUrl = endpointUrl;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
        this.replayCache = new HashMap<>();
    }

    public CompletableFuture<Boolean> connect() {
        return CompletableFuture.supplyAsync(() -> {
            minioClient = MinioClient.builder()
                    .endpoint(endpointUrl)
                    .credentials(accessKey, secretKey)
                    .build();

            try {
                minioClient.bucketExists(
                        BucketExistsArgs.builder()
                                .bucket(bucketName)
                                .build()
                );
            } catch (Exception e) {
                return false;
            }

            return true;
        });
    }

    @Override
    public void saveReplay(Replay replay) {
        CompletableFuture.runAsync(() -> {
            // 1. Write replay to temporary local file (taken from DefaultReplaySaver)
            File localReplayFile = new File(
                    ReplaySystem.getInstance().getDataFolder(),
                    UUID.randomUUID().toString() + ".replaytmp"
            );

            if (!localReplayFile.exists()) {
                try {
                    localReplayFile.createNewFile();

                    FileOutputStream fileOutputStream = new FileOutputStream(localReplayFile);
                    GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);

                    objectOutputStream.writeObject(replay.getData());
                    objectOutputStream.flush();

                    objectOutputStream.close();
                    gzipOutputStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // 2. Upload file to S3 backend
            try {
                minioClient.uploadObject(
                        UploadObjectArgs.builder()
                                .bucket(bucketName)
                                .object(replay.getId() + ".replay")
                                .filename(localReplayFile.getAbsolutePath())
                                .build()
                );
            } catch (Exception exception) {
                ReplaySystem.getInstance().getLogger().log(Level.SEVERE, "Could not upload replay to S3 backend!");
                exception.printStackTrace();
            }

            // 3. Delete temporary local file
            localReplayFile.delete();
        });
    }

    @Override
    public void loadReplay(String replayName, Consumer<Replay> consumer) {

    }

    @Override
    public boolean replayExists(String replayName) {
        return false;
    }

    @Override
    public void deleteReplay(String replayName) {

    }

    @Override
    public List<String> getReplays() {
        return null;
    }
}
