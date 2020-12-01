package org.apache.zookeeper;

import org.apache.zookeeper.common.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Enclosed.class)
public class StringUtilsTest {


    public static class SplitTest{

        String value;
        String separator;
        Object result;

        public SplitTest(String value, String separator, Object result) {
            this.value = value;
            this.separator = separator;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection splitParameters(){

            return Arrays.asList(new Object[][]{

                    {null, null, NullPointerException.class},
                    {"", "", Arrays.asList()},
                    {"s", "st", Arrays.asList("s")},
                    {"a not so long string", "s", Arrays.asList("a not", "o long", "tring")},


                    /*{null, null, NullPointerException.class},
                    {"string", null, NullPointerException.class},
                    {null, "s", NullPointerException.class},
                    {"veryshortstring", "s", Arrays.asList("very", "hort", "tring")},
                    {"shorts", "s", Arrays.asList("hort")}
                    */

            });

        }

        @Test
        public void splitTest(){

            try{

                List<String> list = StringUtils.split(value, separator);
                Assert.assertEquals(result, list);

            }catch (Exception e){

                Assert.assertEquals(e.getClass(), result);

            }

        }

    }

    @RunWith(Parameterized.class)
    public static class JoinStringsTest{

        List<String> list;
        String delimiter;
        Object result;

        public JoinStringsTest(List<String> list, String delimiter, Object result) {
            this.list = list;
            this.delimiter = delimiter;
            this.result = result;
        }

        @Parameterized.Parameters
        public static Collection joinParameters(){

            return Arrays.asList(new Object[][]{

                    {null, null, null},
                    {createList(0), "", IndexOutOfBoundsException.class},
                    {createList(1), "s", "string1"},
                    {createList(2), "rt", "string1rtstring2"}

                    /*
                    {null, "s", null},
                    {createList(0), "s", IndexOutOfBoundsException.class},
                    {createList(3), "s", "string1sstring2sstring3"},
                    {createList(4), "-", "string1-string2-string3-string4"},
                    {createList(3), "", "string1string2string3"}
                    */

            });

        }

        @Test
        public void joinStringsTest(){

            try{

                String joinedString = StringUtils.joinStrings(list, delimiter);
                Assert.assertEquals(result, joinedString);

            }catch (Exception e){

                Assert.assertEquals(e.getClass(), result);

            }

        }

        public static List<String> createList(int numberOfStrings){

            List<String> list = new ArrayList<String>();
            for(int i = 1; i< numberOfStrings+1; i++){

                list.add("string" + i);

            }

            return list;
        }

    }

}

