package ru.DmN.llml.test;

import ru.DmN.llml.compiler.Compiler;
import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.Precompiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Tests {
    public static void main(String[] args) throws IOException {
        var file = new File("test/log");
        if (file.exists()) {
            Arrays.stream(file.listFiles()).forEach(it -> it.delete());
            file.delete();
        }
        file.mkdir();
        //
        Arrays.stream(new File("test").listFiles()).forEach(it -> {
            if (it.isFile()) {
                try {
                    var name = it.getName();
                    if (name.endsWith(".src")) {
                        test(name.substring(0, name.length() - 4));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void test(String name) throws IOException {
        String src;
        try (var stream = new FileInputStream("test/" + name + ".src")) {
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
}
