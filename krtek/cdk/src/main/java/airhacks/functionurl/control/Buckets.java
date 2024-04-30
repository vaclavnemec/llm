package airhacks.functionurl.control;

import software.amazon.awscdk.services.s3.BlockPublicAccess;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;

public interface Buckets {
    

    public static Bucket create(Construct scope){
        return Bucket.Builder.create(scope, "KrtekState")
        .blockPublicAccess(BlockPublicAccess.BLOCK_ALL)
        .bucketName("tietoevry-krtek-state")
        .build();
    }   
}
