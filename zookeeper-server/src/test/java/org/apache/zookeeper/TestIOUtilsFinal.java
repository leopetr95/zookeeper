package org.apache.zookeeper;

import org.apache.zookeeper.common.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;

import static org.apache.zookeeper.common.IOUtils.cleanup;
import static org.apache.zookeeper.common.IOUtils.copyBytes;
import static org.mockito.Mockito.*;

@RunWith(Enclosed.class)
public class TestIOUtilsFinal {

    /*@RunWith(PowerMockRunner.class)
    @PrepareForTest({IOUtils.class})
    public static class mockPrintCopyBytes2Test {

        @Test
        public void testCopyBytes2False() {

            try {

                OutputStream outputStream = createOutputStream();
                PrintStream printStreamMock = mock(PrintStream.class);
                when(printStreamMock.checkError()).thenReturn(false);
                PowerMockito.whenNew(PrintStream.class).withArguments(outputStream).thenReturn(printStreamMock);

                copyBytes(createInputStream(), outputStream, 50);

            } catch (Exception e) {

                e.printStackTrace();
                Assert.assertEquals(NullPointerException.class, e.getClass());
            }


        }

        @Test
        public void testCopyBytes2True() {

            try {

                PrintStream printStreamMock = mock(PrintStream.class);
                when(printStreamMock.checkError()).thenReturn(Boolean.TRUE);
                PowerMockito.whenNew(PrintStream.class).withAnyArguments().thenReturn(printStreamMock);

                System.out.println("fdsf" + printStreamMock.checkError());
                OutputStream outputStream = createOutputStream();
                copyBytes(createInputStream(), outputStream, 50);

            } catch (Exception e) {

                e.printStackTrace();
                Assert.assertEquals(NullPointerException.class, e.getClass());
            }


        }
    }*/

    public static class cleanUpMockTest {

        static final Logger log = LoggerFactory.getLogger(TestIOUtilsFinal.class);

        @Test
        public void tryLogNotNullCleanUpTest() {

            try {

                OutputStream outputStream = Mockito.mock(OutputStream.class);
                doThrow(new IOException()).when(outputStream).close();

                cleanup(log, outputStream);
                Mockito.verify(outputStream, times(1)).close(); // make sure #close method is called once

                /*
                 * the only way to check if the stream is closed is trying to write into it,
                 * if the stream is closed a IOException is raised, if the method write doesn't
                 * raise an exception the stream is not closed
                 * */
                outputStream.write(10);

            } catch (Exception e) {

                e.printStackTrace();
                Assert.assertEquals(IOException.class, e.getClass());
            }
        }

        @Test
        public void tryLogNullCleanUpTest() {

            try {

                OutputStream outputStream = Mockito.mock(OutputStream.class);
                doThrow(new IOException()).when(outputStream).close();

                cleanup(null, outputStream);
                Mockito.verify(outputStream, times(1)).close(); // make sure #close method is called once

                /*
                 * the only way to check if the stream is closed is trying to write into it,
                 * if the stream is closed a IOException is raised, if the method write doesn't
                 * raise an exception the stream is not closed
                 * */
                outputStream.write(10);

            } catch (Exception e) {

                e.printStackTrace();
                Assert.assertEquals(IOException.class, e.getClass());
            }
        }

        @Test
        public void mockOutputCopyBytes1False(){

            try{

                OutputStream outputStream = Mockito.mock(OutputStream.class);

                doThrow(new IOException()).when(outputStream).close();

                copyBytes(createInputStream(), outputStream, 50, false);

                outputStream.write(10);

                Assert.assertTrue(true);
                System.out.println("Sono qui");

            }catch (Exception e){

                System.out.println("Sono invece qui");
                e.printStackTrace();
                Assert.assertEquals(IOException.class, e.getClass());
            }
        }

        @Test
        public void mockOutputCopyBytes1True(){

            try{

                OutputStream outputStream = Mockito.mock(OutputStream.class);
                doThrow(new IOException()).when(outputStream).close();
                copyBytes(createInputStream(), outputStream, 50, true);
                outputStream.write(10);

            }catch (Exception e){

                e.printStackTrace();
                Assert.assertEquals(IOException.class, e.getClass());

            }
        }

    }

    @RunWith(Parameterized.class)
    public static class CloseStreamTest {

        OutputStream stream;
        Object result;

        public CloseStreamTest(OutputStream stream, Object result) {
            this.stream = stream;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection closeStreamParameters() {

            return Arrays.asList(new Object[][]{

                    {null, NullPointerException.class},
                    {createOutputStream(), IOException.class},
                    //{new Exception(), IllegalAccessException.class}


            });

        }

        @Test
        public void closeStreamTest() {

            try {

                IOUtils.closeStream(stream);
                stream.write(10);  //the only way to check if the stream is closed is trying to write into it, if the stream is closed a IOException is raised

            } catch (Exception e) {

                e.printStackTrace();
                Assert.assertEquals(result, e.getClass());
            }


        }

    }

    @RunWith(Parameterized.class)
    public static class CleanUpTest {

        Logger logger;
        OutputStream[] closeables;
        Object result;

        static final Logger log = LoggerFactory.getLogger(TestIOUtilsFinal.class);

        public CleanUpTest(Logger logger, OutputStream[] closeables, Object result) {
            this.logger = logger;
            this.closeables = closeables;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection cleanUpParameters() {

            return Arrays.asList(new Object[][]{

                    {null, null, NullPointerException.class},
                    {log, new OutputStream[]{}, NullPointerException.class},
                    {log, new OutputStream[]{createOutputStream()}, IOException.class},

                    //{new Exception(), new Exception(), IllegalArgumentException.class}

            });

        }

        @Test
        public void cleanUpTest() {

            try {

                cleanup(logger, closeables);
                for (OutputStream closeable : closeables) {

                    closeable.write(10);  //the only way to check if the stream is closed is trying to write into it, if the stream is closed a IOException is raised

                }

            } catch (Exception e) {

                e.printStackTrace();
                Assert.assertEquals(result, e.getClass());

            }

        }

    }

    @RunWith(Parameterized.class)
    public static class CopyBytes1Test {

        InputStream inputStream;
        OutputStream outputStream;
        Integer buffSize;
        boolean close;
        Object result;

        public CopyBytes1Test(InputStream inputStream, OutputStream outputStream, Integer buffSize, boolean close, Object result) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
            this.buffSize = buffSize;
            this.close = close;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection copyBytes1Parameters() {

            return Arrays.asList(new Object[][]{

                    //inputstream, outputstream, buffsize, close, result
                    {null, null, 0, false, NullPointerException.class},
                    {createInputStream(), createOutputStream(), 1, true, true},

            });

        }

        @Test
        public void copyBytes1Test() {

            try {

                File file = new File("/home/leonardo/Desktop/Università/ISW2DeAngelis/archive/SpringMVC17/zookeeper/zookeeper-server/src/main/resources/inPutFile.txt");
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String strInput = bufferedReader.readLine();
                System.out.println(strInput);

                IOUtils.copyBytes(inputStream, outputStream, buffSize, close);

                File file1 = new File("/home/leonardo/Desktop/Università/ISW2DeAngelis/archive/SpringMVC17/zookeeper/zookeeper-server/src/main/resources/outPutFile.txt");
                BufferedReader bufferedReader1 = new BufferedReader(new FileReader(file1));
                String strOutput = bufferedReader1.readLine();
                System.out.println(strOutput);

                boolean flag;

                if (strInput.equals(strOutput)) {

                    flag = true;

                } else {

                    flag = false;

                }

                Assert.assertEquals(result, flag);


            } catch (Exception e) {

                e.printStackTrace();
                Assert.assertEquals(result, e.getClass());

            }


        }

        @After
        public void closeFiles() {

            cleanup(null, inputStream, outputStream);
            System.out.println("Streams closed");

        }

    }

    @RunWith(Parameterized.class)
    public static class CopyBytes2Test {

        InputStream inputStream;
        OutputStream outputStream;
        Integer buffSize;
        Object result;

        public CopyBytes2Test(InputStream inputStream, OutputStream outputStream, Integer buffSize, Object result) {
            this.inputStream = inputStream;
            this.outputStream = outputStream;
            this.buffSize = buffSize;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection copyBytes2Parameters() {

            return Arrays.asList(new Object[][]{

                    {null, null, 0, NullPointerException.class},
                    {createInputStream(), createOutputStream(), 1, true},

                    /*for mutation, the only way to kill the changed mutant on the condition while(bytesRead >=0) is by
                    * having the method read returning 0, this is only possible when the buffer passed as parameter has a length of zero.
                    * However in this case putting a buffer of size zero causes an infinite loop, so it's impossible to test
                     */
                    //{createInputStream(), createOutputStream(), 0, true}


            });

        }

        @Test
        public void copyBytes2Test() {

            try {

                File file = new File("/home/leonardo/Desktop/Università/ISW2DeAngelis/archive/SpringMVC17/zookeeper/zookeeper-server/src/main/resources/inPutFile.txt");
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String strInput = bufferedReader.readLine();
                System.out.println(strInput);

                IOUtils.copyBytes(inputStream, outputStream, buffSize);

                File file1 = new File("/home/leonardo/Desktop/Università/ISW2DeAngelis/archive/SpringMVC17/zookeeper/zookeeper-server/src/main/resources/outPutFile.txt");
                BufferedReader bufferedReader1 = new BufferedReader(new FileReader(file1));
                String strOutput = bufferedReader1.readLine();
                System.out.println(strOutput);

                boolean flag;

                if (strInput.equals(strOutput)) {

                    flag = true;

                } else {

                    flag = false;

                }

                Assert.assertEquals(result, flag);


            } catch (Exception e) {

                e.printStackTrace();
                Assert.assertEquals(result, e.getClass());

            }

        }

        @After
        public void closeFiles() {

            cleanup(null, inputStream, outputStream);
            System.out.println("Streams closed");

        }


    }

    public static InputStream createInputStream() {

        InputStream inputStream = null;

        try {

            File file = new File("/home/leonardo/Desktop/Università/ISW2DeAngelis/archive/SpringMVC17/zookeeper/zookeeper-server/src/main/resources/inPutFile.txt");
            file.createNewFile();
            inputStream = new FileInputStream(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;

    }

    public static OutputStream createOutputStream() {

        OutputStream outputStream = null;
        try {

            File file = new File("/home/leonardo/Desktop/Università/ISW2DeAngelis/archive/SpringMVC17/zookeeper/zookeeper-server/src/main/resources/outPutFile.txt");
            file.createNewFile();
            outputStream = new FileOutputStream(file);

        } catch (IOException e) {

            e.printStackTrace();

        }

        return outputStream;

    }

}
