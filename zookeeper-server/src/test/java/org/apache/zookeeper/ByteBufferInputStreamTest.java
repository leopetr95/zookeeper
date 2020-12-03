package org.apache.zookeeper;

import org.apache.jute.Record;
import org.apache.zookeeper.server.ByteBufferInputStream;
import org.apache.zookeeper.server.ByteBufferOutputStream;
import org.apache.zookeeper.txn.CreateTxn;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Enclosed.class)

public class ByteBufferInputStreamTest {

    @RunWith(Parameterized.class)
    public static class readTest{

        ByteBuffer byteBuffer;
        Object result;

        public readTest(ByteBuffer byteBuffer, Object result) {
            this.byteBuffer = byteBuffer;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection readParameters(){

            return Arrays.asList(new Object[][]{

                    {null, NullPointerException.class},
                    {ByteBuffer.allocate(0), -1},
                    {ByteBuffer.allocate(1), 0},

            });

        }

        @Test
        public void testRead(){

            try {

                ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
                int bytesRead = byteBufferInputStream.read();

                Assert.assertEquals(result, bytesRead);

            } catch (Exception e) {

                Assert.assertEquals(result, e.getClass());
            }
        }

    }

    public static class availableTest{

        @Test
        public void TestAvailable(){

            try {
                ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
                int bytesRead = byteBufferInputStream.available();
                Assert.assertEquals(1000, bytesRead);

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }

    @RunWith(Parameterized.class)
    public static class read1Test{

        ByteBuffer byteBuffer;
        byte[] bytes;
        int offset;
        int length;

        Object result;

        public read1Test(ByteBuffer byteBuffer, byte[] bytes, int offset, int length, Object result) {
            this.byteBuffer = byteBuffer;
            this.bytes = bytes;
            this.offset = offset;
            this.length = length;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection read1Parameters(){

            return Arrays.asList(new Object[][]{

                    //Bytebuffer, bytes, offset, length, result
                    {null, null, 0, 0, NullPointerException.class},
                    {ByteBuffer.allocate(0), new byte[0], 1, 1, -1},
                    {ByteBuffer.allocate(1), new byte[1], 0, 1, 1},

                    //coverage
                    {ByteBuffer.allocate(10), new byte[10], 0, 11, 10}

            });
        }

        @Test
        public void testRead1(){

            try {

                ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
                int bytesRead = byteBufferInputStream.read(bytes, offset, length);
                Assert.assertEquals(result, bytesRead);

            }catch (Exception e) {

                Assert.assertEquals(result, e.getClass());
            }

        }

    }

    @RunWith(Parameterized.class)
    public static class read2Test {

        byte[] bytes;
        Object result;

        public read2Test(byte[] bytes, Object result) {
            this.bytes = bytes;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection read2Parameters(){

            return Arrays.asList(new Object[][]{


                    {null, NullPointerException.class},
                    {new byte[0], 0},
                    {new byte[1], 1}

            });

        }

        @Test
        public void testRead2(){

            try {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
                int bytesRead = byteBufferInputStream.read(bytes);

                Assert.assertEquals(result, bytesRead);

            } catch (Exception e) {

                Assert.assertEquals(result, e.getClass());

            }

        }

    }

    @RunWith(Parameterized.class)
    public static class skipTest{

        ByteBuffer byteBuffer;
        long n;
        Object result;

        public skipTest(ByteBuffer byteBuffer, long n, Object result) {
            this.byteBuffer = byteBuffer;
            this.n = n;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection skipParameters(){

            return Arrays.asList(new Object[][]{

                    //ByteBuffer, n, result
                    {null, 0, NullPointerException.class},
                    {ByteBuffer.allocate(0), 1, (long) 0},
                    {ByteBuffer.allocate(1), 1, (long) 1},

                    //coverage
                    {ByteBuffer.allocate(1), -1, (long) 0},
                    {ByteBuffer.allocate(1), 0L, 0L},
                    {ByteBuffer.allocate(5), 10, (long)5}

            });

        }

        @Test
        public void testSkip(){

            try {

                ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
                long skipResult = byteBufferInputStream.skip(n);

                Assert.assertEquals(result, skipResult);

            } catch (Exception e) {

                Assert.assertEquals(result, e.getClass());
            }
        }

    }

    @RunWith(Parameterized.class)
    public static class byteBuffer2RecordTest{

        ByteBuffer byteBuffer;
        Record record;
        Object result;

        public byteBuffer2RecordTest(ByteBuffer byteBuffer, Record record, Object result) {
            this.byteBuffer = byteBuffer;
            this.record = record;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection byteBuffer2RecordParameters(){

            return Arrays.asList(new Object[][]{

                    {null, null, NullPointerException.class},
                    {ByteBuffer.allocate(0), new CreateTxn(), BufferOverflowException.class},
                    {ByteBuffer.allocate(100), new CreateTxn(), "0"},

            });

        }

        @Test
        public void TestByteBuffer2Record(){

            try {

                //serializing
                ByteBufferOutputStream.record2ByteBuffer(record, byteBuffer);

                //deserializing
                ByteBufferInputStream.byteBuffer2Record(byteBuffer, record);

                //checking if byteBuffer is deserialized correctly by serializing it again
                //serializing
                ByteBufferOutputStream.record2ByteBuffer(record, byteBuffer);

            } catch (Exception e) {

                Assert.assertEquals(result, e.getClass());

            }


        }



    }

}
