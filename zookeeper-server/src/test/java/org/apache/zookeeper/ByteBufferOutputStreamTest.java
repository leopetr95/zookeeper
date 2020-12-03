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
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;

import static org.apache.zookeeper.server.ByteBufferInputStream.byteBuffer2Record;
import static org.apache.zookeeper.server.ByteBufferOutputStream.record2ByteBuffer;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class ByteBufferOutputStreamTest {

    public static byte[] createByteArray(int length) {

        byte[] byteArray = new byte[length];
        for (int i = 0; i < length; i++) {

            byteArray[i] = (byte) 1;

        }

        return byteArray;

    }

    @RunWith(Parameterized.class)
    public static class writeTest {

        int b;
        Object result;

        public writeTest(int b, Object result) {
            this.b = b;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection writeParameters() {

            return Arrays.asList(new Object[][]{

                    {0, (byte) 0},
                    {1, (byte) 1}

            });

        }

        @Test
        public void testWrite() {

            try {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                ByteBufferOutputStream byteBufferOutputStream = new ByteBufferOutputStream(byteBuffer);
                byteBufferOutputStream.write(b);

                byteBuffer.position(0);
                Assert.assertEquals(result, byteBuffer.get());

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    @RunWith(Parameterized.class)
    public static class write1Test {

        byte[] b;
        Object result;

        public write1Test(byte[] b, Object result) {
            this.b = b;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection write1Parameters() {

            return Arrays.asList(new Object[][]{

                    {null, NullPointerException.class},
                    {createByteArray(0), (byte) 0},
                    {createByteArray(1), (byte) 1}

            });

        }

        @Test
        public void testWrite1() {

            try {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                ByteBufferOutputStream byteBufferOutputStream = new ByteBufferOutputStream(byteBuffer);
                byteBufferOutputStream.write(b);

                byteBuffer.position(0);
                Assert.assertEquals(result, byteBuffer.get());

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    @RunWith(Parameterized.class)
    public static class write2Test {


        byte[] b;
        int offset;
        int length;
        Object result;

        public write2Test(byte[] b, int offset, int length, Object result) {
            this.b = b;
            this.offset = offset;
            this.length = length;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection readParameters() {

            return Arrays.asList(new Object[][]{

                    {null, 0, 0, NullPointerException.class},
                    {createByteArray(0), 1, 1, IndexOutOfBoundsException.class},
                    {createByteArray(1), 0, 1, (byte) 1}

            });

        }

        @Test
        public void testWrite2() {

            try {

                ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
                ByteBufferOutputStream byteBufferOutputStream = new ByteBufferOutputStream(byteBuffer);
                byteBufferOutputStream.write(b, offset, length);

                byteBuffer.position(0);
                Assert.assertEquals(result, byteBuffer.get());

            } catch (Exception e) {

                e.printStackTrace();

            }
        }
    }

    @RunWith(Parameterized.class)
    public static class record2ByteBufferTest{

       ByteBuffer byteBuffer;
       Record record;
       Object result;

        public record2ByteBufferTest(ByteBuffer byteBuffer, Record record, Object result) {
            this.byteBuffer = byteBuffer;
            this.record = record;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection record2ByteBufferParameters() {

            return Arrays.asList(new Object[][]{

                    {null, null, NullPointerException.class},
                    {ByteBuffer.allocate(0), new CreateTxn(), BufferOverflowException.class},
                    {ByteBuffer.allocate(100), new CreateTxn(), null}

            });

        }

        @Test
        public void testRecord2ByteBuffer(){

            try {

                //serializing
                ByteBufferOutputStream.record2ByteBuffer(record, byteBuffer);

                //checking if byteBuffer is serialized correctly by deserializing it
                //deserializing
                ByteBufferInputStream.byteBuffer2Record(byteBuffer, record);

            } catch (Exception e) {

                e.printStackTrace();
                Assert.assertEquals(result, e.getClass());
            }

        }
    }

}
