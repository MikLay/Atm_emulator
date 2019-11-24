package com.atm.controller;

import com.atm.controller.auth.BasicAuthenticatorImpl;
import com.atm.controller.handler.AuthHandler;
import com.atm.controller.handler.TransactionTransferCreatorHandler;
import com.atm.controller.handler.TransactionsHandler;
import com.atm.model.QueriesManager;
import com.atm.model.dao.BankAccountDao;
import com.atm.model.dao.ClientDao;
import com.atm.model.dao.TransactionDao;
import com.atm.model.dao.impl.BankAccountDaoImpl;
import com.atm.model.dao.impl.ClientDaoImpl;
import com.atm.model.dao.impl.TransactionDaoImpl;
import com.atm.model.service.BankAccountService;
import com.atm.model.service.TransactionService;
import com.atm.model.service.impl.BankAccountServiceImpl;
import com.atm.model.service.impl.TransactionServiceImpl;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.internal.util.io.StreamCopier.BUFFER_SIZE;

public class HttpServerAtm {
    private static BankAccountService bankAccountService;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(3005), 0);

        BankAccountDao accountDao = new BankAccountDaoImpl(QueriesManager.getProperties("account"));
        ClientDao clientDao = new ClientDaoImpl(QueriesManager.getProperties("client"));
        TransactionDao transactionDao = new TransactionDaoImpl(QueriesManager.getProperties("transaction"));

        bankAccountService = new BankAccountServiceImpl(accountDao);
        TransactionService transactionService = new TransactionServiceImpl(transactionDao);

        HttpContext authContext = server.createContext("/auth", new AuthHandler(bankAccountService));
        authContext.setAuthenticator(new BasicAuthenticatorImpl("auth", bankAccountService));

        HttpContext transactionsContext = server.createContext("/transactions", new TransactionsHandler(bankAccountService));
        transactionsContext.setAuthenticator(new BasicAuthenticatorImpl("transactions", bankAccountService));

        HttpContext createTransactionContext = server.createContext("/transaction", new TransactionTransferCreatorHandler(bankAccountService, transactionService));
        createTransactionContext.setAuthenticator(new BasicAuthenticatorImpl("transaction", bankAccountService));

        server.setExecutor(null);

        server.start();
        System.out.println("The server is running");
    }

    public static void writeSuccessResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        try {
            byte[] bs = response.getBytes(StandardCharsets.UTF_8);
            httpExchange.sendResponseHeaders(200, bs.length);
            OutputStream os = httpExchange.getResponseBody();
            os.write(bs);
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void writeSuccessCreateResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(201, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static void writeAccesDeniedResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(403, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static void writeDataErroResponse(HttpExchange httpExchange, String response, Integer errorNumber) throws IOException {
        httpExchange.sendResponseHeaders(errorNumber, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static void writeBadResponse(HttpExchange httpExchange, String response) throws IOException {
        httpExchange.sendResponseHeaders(404, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static boolean addResponses(HttpExchange httpExchange) throws IOException {
        httpExchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        if (httpExchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
            httpExchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type,Authorization");
            httpExchange.sendResponseHeaders(204, -1);
            return true;
        }
        return false;
    }

    public static String[] parseToken(String token) {
        return token.split("\\+");
    }

    public static Map<String, String> queryToMap(String query) {

        Map<String, String> result = new HashMap<>();
        System.out.println("awd");
        try {

            for (String param : query.split("[&]")) {
                String[] pair = param.replaceAll("\\+", " ").split("=");
                if (pair.length > 1) {
                    result.put(pair[0], pair[1]);
                } else {
                    result.put(pair[0], "");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result.toString());
        return result;
    }

}
