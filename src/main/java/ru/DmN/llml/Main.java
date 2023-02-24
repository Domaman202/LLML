package ru.DmN.llml;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import ru.DmN.llml.compiler.Compiler;
import ru.DmN.llml.lexer.Lexer;
import ru.DmN.llml.parser.InvalidTokenException;
import ru.DmN.llml.parser.Parser;
import ru.DmN.llml.precompiler.Precompiler;
import ru.DmN.llml.utils.OptimizationConfig;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        var parser = ArgumentParsers.newFor("llml").build().defaultHelp(true).description("[Low Level Math Language]");
        parser.usage("llml [options] src");
        parser.addArgument("-o")
                .nargs(1).type(String.class).metavar("<file>")
                .help("записывает результат в <file>");
        parser.addArgument("-ast")
                .nargs("*").type(Boolean.class)
                .help("вывод ast в консоль");
        parser.addArgument("src")
                .nargs(1).type(String.class)
                .help("файл с исходным кодом");
        try {
            compile(parser.parseArgs(args));
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }

    private static void compile(Namespace args) {
        try {
            String src = args.getString("src");
            src = src.substring(1, src.length() - 1);
            boolean ast = args.get("ast") != null;
            String out = args.getString("o");
            out = out == null ? src.substring(Math.max(-1, src.lastIndexOf('/') + 1)) + ".ll" : out.substring(1, out.length() - 1);

            String code = null;
            try (var file = new FileInputStream(src)) {
                code = new String(file.readAllBytes());
            } catch (IOException e) {
                System.err.println("Произошла ошибка при считывании исходного кода!\n" + e.getMessage());
                System.exit(1);
            }

            var lexer = new Lexer(code);
            var parser = new Parser(lexer);
            var ctx = parser.parse();

            if (ast) {
                System.out.println("Parsed:\n" + ctx.print(0));
            }

            var precompiler = new Precompiler(ctx);
            precompiler.precompile();

            if (ast) {
                System.out.println("\nPrecompiled:\n" + ctx.print(0));
            }

            var compiler = new Compiler(ctx);
            compiler.compile(new OptimizationConfig(true));

            if (ast) {
                System.out.println("\nCompiled:");
                System.out.println(compiler.out);
            }

            try (var stream = new FileOutputStream(out)) {
                stream.write(compiler.out.toString().getBytes());
            } catch (IOException e) {
                System.err.println("Произошла ошибка при записи результата!\n" + e.getMessage());
                System.exit(1);
            }
        } catch (InvalidTokenException exception) {
//            System.err.println(exception.getMessage());
            exception.printStackTrace();
            System.exit(1);
        }
    }
}