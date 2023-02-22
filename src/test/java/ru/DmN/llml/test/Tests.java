package ru.DmN.llml.test;

import com.google.gson.Gson;
import ru.DmN.llml.compiler.Compiler;
import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.Precompiler;

import java.io.*;
import java.util.Arrays;

public class Tests {
    public static final Config config;

    public static void main(String[] args) {
        var file = new File("test/log");
        if (file.exists()) {
            Arrays.stream(file.listFiles()).forEach(it -> it.delete());
            file.delete();
        }
        file.mkdir();
        //
        Arrays.stream(new File("test/src").listFiles()).forEach(it -> {
            var name = it.getName();
            name = name.substring(0, name.length() - 4);
            try {
                test(name);
            } catch (IOException e) {
                new RuntimeException("Ошибка при выполнении теста \"" + name + "\":\n" + e.getMessage()).printStackTrace();
//                e.printStackTrace();
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
//                e.printStackTrace();
            }
        });
    }

    private static void test(String name) throws IOException {
        String src;
        try (var stream = new FileInputStream("test/src/" + name + ".src")) {
            src = new String(stream.readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при выполнении теста \"" + name + "\"! (Исходники не найдены)");
        }

        var lexer = new Lexer(src);
        var parser = new Parser(lexer);
        var precompiler = new Precompiler(parser.parse());
        var compiler = new Compiler(precompiler.precompile());
        var out = compiler.compile();

        try (var stream = new FileOutputStream("test/log/" + name + ".ll")) {
            stream.write(out.getBytes());
        }

        if (calccheck(out) != loadcheck(name)) {
            throw new RuntimeException("Ошибка при проверке теста \"" + name + "\"!");
        }

        if (config.optimization.enable) {
            var process = Runtime.getRuntime().exec("opt -S -O" + config.optimization.level + " -o test/log/" + name + ".optimized.ll test/log/" + name + ".ll");
            while (process.isAlive()) Thread.onSpinWait();
            var stream = process.getErrorStream();
            if (stream.available() > 0) throw new RuntimeException(new String(stream.readAllBytes()));
        }
    }

    private static int calccheck(String str) {
        return str.chars().sum();
    }

    private static int loadcheck(String name) {
        var sum = 0;
        try (var stream = new FileInputStream("test/checklog/" + name + ".ll")) {
            while (stream.available() > 0) {
                sum += stream.read();
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при выполении теста \"" + name + "\"! (Лог проверки не найден)");
        }
        return sum;
    }

    static {
        var gson = new Gson();
        try (var stream = new FileInputStream("test/config.json")) {
            config = gson.fromJson(new String(stream.readAllBytes()), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
