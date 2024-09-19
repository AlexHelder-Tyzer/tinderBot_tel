package com.codegym.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class TinderBoltApp extends SimpleTelegramBot {

    public static final String TELEGRAM_BOT_TOKEN = "7345872590:AAGsU9Gpicz3SDAaswObY_OChJLzOHkNtqs"; //TODO: añadir el token del bot entre comillas
    public static final String OPEN_AI_TOKEN = "gpt:yeSMIRCC7ePMWDmNqAy9JFkblB3T8MQ6g521gef0dCT5qsls"; //TODO: añadir el token de ChatGPT entre comillas

    private ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode mode;

    public TinderBoltApp() {
        super(TELEGRAM_BOT_TOKEN);
    }

    //TODO: escribiremos la funcionalidad principal del bot aquí

    public void startCommand(){
        mode = DialogMode.MAIN;
        String text = loadMessage("main");
        sendPhotoMessage("main");
        sendTextMessage(text);

        showMainMenu(
                "start", "Menu principal del bot",
                "profile", "Generacion del perfil de Tinder \uD83D\uDE0E",
                "opener", "Mensaje para inicializar conversacion \uD83E\uDD70",
                "message", "Correspondencia en su nombre \uD83D\uDE08",
                "data", "Correspondencia con celebridades \uD83D\uDD25",
                "gpt", "Hacer una pregunta a ChatGPT \uD83E\uDDE0"
        );
    }

    public void gptCommand(){
        mode = DialogMode.GPT;
        String text = loadMessage("gpt");
        sendPhotoMessage("gpt");
        sendTextMessage(text);
    }

    public void gptDialog(){
        String text = getMessageText();
        String prompt = loadPrompt("gpt");
        String answer = chatGPT.sendMessage(prompt, text);
        sendTextMessage(answer);
    }

    // respuestas de bot
    public void hello(){
        if (mode == DialogMode.GPT){
            gptDialog();
        }
        else{
            String text = getMessageText();
            sendTextMessage("*Hello*");
            sendTextMessage("_How are you?_");
            sendTextMessage("You wrote: " + text);

            sendPhotoMessage("avatar_main"); // enviar imagen
            sendTextButtonsMessage("Launch Process",
                    "Start", "Start",
                    "Stop", "Stop"); // dos botones
        }
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
        addCommandHandler("gpt", this::gptCommand);
        addMessageHandler(this::hello);
        addButtonHandler("^.*", this::helloButton);
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
