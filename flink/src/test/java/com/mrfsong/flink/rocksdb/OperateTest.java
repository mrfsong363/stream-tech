package com.mrfsong.flink.rocksdb;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.rocksdb.*;
import org.rocksdb.util.SizeUnit;

/**
 * <p>
 *      示例用法 @see https://github.com/facebook/rocksdb/tree/master/java/samples/src/main/java
 * </P>
 *
 * @Author songfei20
 * @Date 2019/11/19
 */
@Slf4j
public class OperateTest {


    static {
        //Load RocksDb library
        RocksDB.loadLibrary();
    }

    @Test
    public void testQuery() {


        String tmpPath = System.getProperty("java.io.tmpdir");
        log.debug("Temp dir path : {}" , tmpPath);


        Options options = new Options();
        Filter bloomFilter = new BloomFilter(10);
        ReadOptions readOptions = new ReadOptions().setFillCache(false);
        Statistics stats = new Statistics();
        RateLimiter rateLimiter = new RateLimiter(10000000,10000, 10);

        RocksDB db = null;
        try {

            options.setCreateIfMissing(true)
                    .setStatistics(stats)
                    .setWriteBufferSize(8 * SizeUnit.KB)
                    .setMaxWriteBufferNumber(3)
                    .setMaxBackgroundCompactions(10)
                    .setCompressionType(CompressionType.SNAPPY_COMPRESSION)
                    .setCompactionStyle(CompactionStyle.UNIVERSAL);

            options.setMemTableConfig(
                    new HashSkipListMemTableConfig()
                            .setHeight(4)
                            .setBranchingFactor(4)
                            .setBucketCount(2000000));


            db = RocksDB.open(options, tmpPath);
            db.put("hello".getBytes(), "rocksdb".getBytes());




        } catch (RocksDBException e) {
            e.printStackTrace();
        }finally {
            if(db != null){
                db.close();
            }
        }


    }

}
