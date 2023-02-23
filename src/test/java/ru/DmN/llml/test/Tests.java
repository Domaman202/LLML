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
        var file = new File("test/log");
        if (file.exists()) {
            Arrays.stream(file.listFiles()).forEach(File::delete);
            file.delete();
        }
        file.mkdir();
        //
        Arrays.stream(new File("test/src").listFiles()).forEach(it -> {
            var name = it.getName();
            if (name.endsWith(".json")) {
                name = name.substring(0, name.length() - 5);
                TestConfig config;
                try (var stream = new FileInputStream("test/src/" + name + ".json")) {
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
                } catch (RuntimeException e) {
                    System.err.println(e.getMessage());
//                e.printStackTrace();
                }
            }
        });
    }

    private static void test(TestConfig config) throws IOException {
        String src;
        try (var stream = new FileInputStream("test/src/" + config.src)) {
            src = new String(stream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при выполнении теста \"" + config.name + "\"! (Исходники не найдены)");
        }

        var lexer = new Lexer(src);
        var parser = new Parser(lexer);
        var precompiler = new Precompiler(parser.parse());
        var compiler = new Compiler(precompiler.precompile());
        var out = compiler.compile(config.optimization);

        try (var stream = new FileOutputStream("test/log/" + config.out)) {
            stream.write(out.getBytes());
        }

        if (calccheck(out) != loadcheck(config)) {
            throw new RuntimeException("Ошибка при проверке теста \"" + config.name + "\"!");
        }

        var process = Runtime.getRuntime().exec("opt -S -O" + Tests.config.optimization + " -o test/log/" + config.out + ".optimized test/log/" + config.out);
        while (process.isAlive()) Thread.onSpinWait();
        var stream = process.getErrorStream();
        if (stream.available() > 0) throw new RuntimeException(new String(stream.readAllBytes()));
    }

    private static int calccheck(String str) {
        return str.chars().sum();
    }

    private static int loadcheck(TestConfig config) {
        var sum = 0;
        try (var stream = new FileInputStream("test/checklog/" + config.out)) {
            while (stream.available() > 0) {
                sum += stream.read();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при выполении теста \"" + config.name + "\"! (Лог проверки не найден)");
        }
        return sum;
    }

    static {
        var gson = new Gson();
        try (var stream = new FileInputStream("test/config.json")) {
            config = gson.fromJson(new String(stream.readAllBytes()), GlobalConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
