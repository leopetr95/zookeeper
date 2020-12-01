package org.apache.zookeeper;

import org.apache.jute.Record;
import org.apache.zookeeper.server.ByteBufferInputStream;
import org.apache.zookeeper.txn.CreateTxn;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

import static org.apache.zookeeper.server.ByteBufferInputStream.byteBuffer2Record;

@RunWith(Enclosed.class)

public class ByteBufferInputStreamTest {

    @RunWith(Parameterized.class)
    public static class readTest{

        byte[] bytes;
        int offset;
        int length;

        Object result;

        public readTest(byte[] bytes, int offset, int length, Object result) {
            this.bytes = bytes;
            this.offset = offset;
            this.length = length;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection readParameters(){

            return Arrays.asList(new Object[][]{

                    {null, 0, 0, NullPointerException.class},
                    {new byte[0], 1, 1, IndexOutOfBoundsException.class},
                    {new byte[1], 0, 1, 1}

            });

        }

        @Test
        public void testRead(){

            try {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
                int bytesRead = byteBufferInputStream.read(bytes, offset, length);

                Assert.assertEquals(result, bytesRead);

            }catch (Exception e) {

                Assert.assertEquals(result, e.getClass());
            }

        }

    }

    @RunWith(Parameterized.class)
    public static class read1Test {

        byte[] bytes;
        Object result;

        public read1Test(byte[] bytes, Object result) {
            this.bytes = bytes;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection read1Parameters(){

            return Arrays.asList(new Object[][]{


                    {null, NullPointerException.class},
                    {new byte[0], 0},
                    {new byte[1], 1}

            });

        }

        @Test
        public void testRead1(){

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

        long n;
        Object result;

        public skipTest(long n, Object result) {
            this.n = n;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection skipParameters(){

            return Arrays.asList(new Object[][]{

                    {0, 0},
                    {1, 1}

            });

        }

        @Test
        public void testSkip(){


            try {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
                byteBufferInputStream.skip(n);

                Assert.assertEquals(result, byteBuffer.position());

            } catch (Exception e) {

                Assert.assertEquals(result, e.getClass());

            }

        }

    }

    public static class read2Test{

        @Test
        public void testRead2(){


            try {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                ByteBufferInputStream byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
                int bytesRead = byteBufferInputStream.read();

                Assert.assertEquals(0, bytesRead);

            } catch (Exception e) {

                e.printStackTrace();

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
    public static class byteBuffer2RecordTest{

        ByteBuffer byteBuffer;
        Record record;
        Object result;

        public byteBuffer2RecordTest(ByteBuffer byteBuffer, Object result) {
            this.byteBuffer = byteBuffer;
            this.record = record;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection byteBuffer2RecordParameters(){

            return Arrays.asList(new Object[][]{

                    {null, NullPointerException.class},
                    {new byte[0], 0},
                    {new byte[1], 1}

            });

        }

        @Test
        public void TestByteBuffer2Record(){

            try {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                Record record = new CreateTxn();
                byteBuffer2Record(byteBuffer, record);

            } catch (IOException e) {

                e.printStackTrace();

            }


        }



    }

}
