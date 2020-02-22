package learning.Other.TESTNG

import org.testng.annotations.Test

@Test(groups = "AllClassTests")
class ListernerTestParallel {

    @Test(threadPoolSize = 2, invocationCount = 4)
    void test1() {
        println("Inside Test 1 (${Thread.currentThread().getId()})")
    }

    @Test
    void test2() {
        println("Inside Test 2 (${Thread.currentThread().getId()})")
    }

    @Test
    void test3() {
        println("Inside Test 3 (${Thread.currentThread().getId()})")
    }

    @Test
    void test4() {
        println("Inside Test 4 (${Thread.currentThread().getId()})")
    }

}
