package com.codegym.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TinderBoltApp extends SimpleTelegramBot {

    public static final String TELEGRAM_BOT_TOKEN = "INSERTAR TOKEN TELGRAM"; //TODO: añadir el token del bot entre comillas
    public static final String OPEN_AI_TOKEN = "chat-gpt-token"; //TODO: añadir el token de ChatGPT entre comillas

    public TinderBoltApp() {
        super(TELEGRAM_BOT_TOKEN);
    }

    //TODO: escribiremos la funcionalidad principal del bot aquí

    public void startCommand(){
        String text = loadMessage("main");
        sendPhotoMessage("main");
        sendTextMessage(text);
    }

    // respuestas de bot
    public void hello(){
        String text = getMessageText();
        sendTextMessage("*Hello*");
        sendTextMessage("_How are you?_");
        sendTextMessage("You wrote: " + text);

        sendPhotoMessage("avatar_main"); // enviar imagen
        sendTextButtonsMessage("Launch Process",
                "Start", "Start",
                "Stop", "Stop"); // dos botones
    }

    // manejo de botones en telgram
    public void helloButton(){
        String key = getButtonKey(); //identificar boton clickado
        if (key.equals("Start")){
            sendTextMessage("The process has been launched");
        }
        else{
            sendTextMessage("Process has been stopped");
        }
    }

    // Todos los metodos creados deben ser inicializados aqui
    @Override
    public void onInitialize() {
        //TODO: y un poco más aquí :)
        addCommandHandler("start", this::startCommand);
        addMessageHandler(this::hello);
        addButtonHandler("^.*", this::helloButton);
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
