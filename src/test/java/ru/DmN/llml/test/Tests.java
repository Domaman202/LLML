package ru.DmN.llml.test;

import com.google.gson.Gson;
import ru.DmN.llml.compiler.Compiler;
import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.Precompiler;

import java.io.*;
import java.util.Arrays;

public class Tests {
    public static final GlobalConfig config;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void main(String[] args) {
        var file = new File("test/tmp");
        if (file.exists()) {
            Arrays.stream(file.listFiles()).forEach(File::delete);
            file.delete();
        }
        file.mkdir();
        //
        Arrays.stream(new File("test/config").listFiles()).forEach(it -> {
            if (it.getName().endsWith(".json")) {
                TestConfig config;
                try (var stream = new FileInputStream(it)) {
                    var gson = new Gson();
                    config = gson.fromJson(new String(stream.readAllBytes()), TestConfig.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    test(config);
                } catch (IOException e) {
                    new RuntimeException("Ошибка при выполнении теста \"" + config.name + "\":\n" + e.getMessage()).printStackTrace();
//                e.printStackTrace();
                } catch (TestException | RuntimeException e) {
                    System.err.println(e.getMessage());
//                e.printStackTrace();
                }
            }
        });
    }

    private static void test(TestConfig config) throws IOException, TestException {
        String src;
        try (var stream = new FileInputStream("test/src/" + config.src + ".llml")) {
            src = new String(stream.readAllBytes());
        } catch (IOException e) {
            throw new TestException("Ошибка при выполнении теста \"" + config.name + "\"! (Исходники не найдены)");
        }

        var lexer = new Lexer(src);
        var parser = new Parser(lexer);
        var precompiler = new Precompiler(parser.parse());
        var compiler = new Compiler(precompiler.precompile());
        var out = compiler.compile();

        var out$ll = "test/tmp/" + config.out + ".ll";
        try (var stream = new FileOutputStream(out$ll)) {
            stream.write(out.getBytes());
        }

        // проверка
        check(config, out);

        // оптимизация
        var out$opt = "test/tmp/" + config.out + ".optimized.ll";
        exec("opt -S -O" + Tests.config.optimization + " -o " + out$opt + ' ' + out$ll);
        // тестирование
        if (config.test != null) {
            for (var test : config.test) {
                exec("clang -o test/tmp/" + config.out + ' ' + out$opt + " test/tsrc/" + test + ".c");
                exec("./test/tmp/" + test + " > test/tmp/" + test + ".log");
            }
        }
    }

    private static void exec(String cmd) throws IOException {
        var process = Runtime.getRuntime().exec(cmd);
        while (process.isAlive()) Thread.onSpinWait();
        var stream = process.getErrorStream();
        if (stream.available() > 0) throw new RuntimeException(new String(stream.readAllBytes()));
    }

    private static void check(TestConfig config, String str) {
        var sum = 0;
        try (var stream = new FileInputStream("test/check/" + config.out + ".ll")) {
            while (stream.available() > 0)
                sum += stream.read();
            if (sum != str.chars().sum())
                System.err.println("Ошибка при проверке теста \"" + config.name + "\"!");
        } catch (IOException e) {
            System.err.println("Ошибка при выполении теста \"" + config.name + "\"! (Лог проверки не найден)");
        }
    }

    static {
        var gson = new Gson();
        try (var stream = new FileInputStream("test/config.json")) {
            config = gson.fromJson(new String(stream.readAllBytes()), GlobalConfig.class);
        } catch (IOException e) {
            throw new Error(e);
        }
    }
}
