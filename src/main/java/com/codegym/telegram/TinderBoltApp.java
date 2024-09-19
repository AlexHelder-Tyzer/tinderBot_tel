package com.codegym.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TinderBoltApp extends SimpleTelegramBot {

    public static final String TELEGRAM_BOT_TOKEN = "TOKEN"; //TODO: añadir el token del bot entre comillas
    public static final String OPEN_AI_TOKEN = "TKEN"; //TODO: añadir el token de ChatGPT entre comillas

    private ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode mode;
    private ArrayList<String> list = new ArrayList<>();

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
        var myMessage = sendTextMessage("gpt is typing...");
        String answer = chatGPT.sendMessage(prompt, text); // sendMessage abre un nuevo chat cada vez
        //sendTextMessage(answer);
        updateTextMessage(myMessage, answer);
    }


    public void dateCommand(){
        mode = DialogMode.DATE;
        String text = loadMessage("date");
        sendPhotoMessage("date");
        sendTextMessage(text);
        sendTextButtonsMessage(text,
                "date_grande", "Ariana Grande",
                "date_robbie", "Margot Robbie",
                "date_zendaya","Zendaya",
                "date_gosling", "Bryan Gosling",
                "date_hardy", "Tom Hardy");
    }

    public void dateButton(){
        String key = getButtonKey();
        sendPhotoMessage(key);
        sendHtmlMessage(key);
        String prompt = loadPrompt(key);
        chatGPT.setPrompt(prompt);
    }

    public void dateDialog(){
        String text = getMessageText();
        var myMessage = sendTextMessage("user is typing...");
        String answer = chatGPT.addMessage(text); // nos mantenemos dentro del mismochat
        //sendTextMessage(answer);
        updateTextMessage(myMessage, answer);
    }

    public void messageCommand(){
        mode = DialogMode.MESSAGE;
        String text = loadMessage("message");
        sendPhotoMessage("message");
        sendTextButtonsMessage(text,
                "message_next", "write next message",
                "message_date", "Ask the person out on a date");
        list.clear();
    }

    public void messageButton(){
        String key = getButtonKey();
        String prompt = loadPrompt(key);
        String history = String.join("\n\n", list);
        var myMessage = sendTextMessage("Chat GPT is typing...");
        String answer = chatGPT.sendMessage(prompt, history);
        updateTextMessage(myMessage, answer);
    }

    public void messageDialog(){
        String text = getMessageText();
        list.add(text);
    }

    // respuestas de bot
    public void hello(){
        if (mode == DialogMode.GPT){
            gptDialog();
        }else if(mode == DialogMode.DATE){
            dateDialog();
        }
        else if(mode == DialogMode.MESSAGE){
            messageDialog();
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
        addCommandHandler("date", this::dateCommand);
        addCommandHandler("message", this::messageCommand);
        addMessageHandler(this::hello);
        //addButtonHandler("^.*", this::helloButton);
        addButtonHandler("date_.*", this::dateButton);
        addButtonHandler("message_.*", this::messageButton);
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
