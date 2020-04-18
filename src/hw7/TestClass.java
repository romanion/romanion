package hw7;

class TestClass {
    private static TestClass instance;//Singleton

    private TestClass() {
    }

    @BeforeSuite
    private static TestClass start() {
        System.out.println("Test started");
        if (instance == null) return new TestClass();
        else return instance;
    }

    @Test(priority = 1)
    private void test() {
        System.out.println("nothing.");
    }

    @Test(priority = 6)
    private void test(int number) {
        System.out.println("number: " + number);
    }

    @Test
    private void test(float number) {
        System.out.println("float: " + number);
    }

    @Test(priority = 4)
    private void test(double number) {
        System.out.println("double: " + number);
    }

    @Test(priority = 7)
    private void test(Boolean bool) {
        System.out.println("bool: " + bool);
    }

    @Test(priority = 2)
    private void test(byte number) {
        System.out.println("byte: " + number);
    }

    @Test
    private void test(String str) {
        System.out.println("string: " + str);
    }

    @Test(priority = 0)
    private void test(String str1, String str2) {
        System.out.println("strings: " + str1 + " " + str2);
    }

    @AfterSuite
    private static void finish() {
        System.out.println("Test finished");
        instance = null;
    }
}

