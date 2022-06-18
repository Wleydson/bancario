package br.com.wleydson.bancario.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ContaControllerTest.class,
        TransferenciaControllerTest.class })
public class RunAllTests {
}
