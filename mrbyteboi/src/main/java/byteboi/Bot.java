package byteboi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ini4j.Ini;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot{
    @Override
    public String getBotUsername() {
        return "ByteBoi_bot";
    }

    @Override
    public String getBotToken() {
        //For Local Development key.ini is used for storing the token.

        // String token = "token";
        // try{
        //     Ini ini = new Ini(new File("mrbyteboi/src/main/java/byteboi/key.ini"));
        //     token = ini.get("header","TOKEN");
        // }catch(Exception e){
        //     System.out.println(e);
        // }

        // for production enviornment variable is used
        String token = System.getenv("TOKEN");

        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {

        BotCommand botCommand = new BotCommand();
        botCommand.setCommand("/test");
        botCommand.setDescription("testing");
        if(update.hasMessage()){
            var msg = update.getMessage();
            var newMember = msg.getNewChatMembers();
            if(msg.isCommand()){

                long commandFrom = msg.getFrom().getId();
                long inChat = msg.getChatId();
                runCommand(msg.getText(), commandFrom, inChat);

            }else if(!newMember.isEmpty()){

                for(var user:newMember){
                    var welcome = String.format("Hello! <a href='tg://user?id=%d'>%s</a> | You're welcome in \n<blockquote><b>%s</b></blockquote> ",
                    user.getId(),
                    user.getFirstName(),
                    msg.getChat().getTitle());
                    sendWelcomeText(msg.getChatId(), welcome);
                }

            }
        }
        
    }

    public void runCommand(String command,long from,long chat){
        switch (command) {
            case "/start rules":
                String rulesAndRegulations = "\t<blockquote><b>Rules and Regulations</b></blockquote>\n" + //
                    "Group : <b>COE Assignments and Notes</b>\n" + //
                    "\n" + //
                    "1. Be respectful to every member of this group.\n" + //
                    "2. Abusing/Bulling/Trolling of any member of this group will result in direct BAN.\n" + //
                    "3. Strictly No Spamming.\n" + //
                    "\n" + //
                    ">> Violation of any of the rules shall lead to WARNING,KICK or BAN from the group.\n" + //
                    "";
                sendMessage(from, rulesAndRegulations);
                break;
        
            default:
                break;
        }
    }
    
    public void sendMessage(Long who,String what){
        SendMessage message = SendMessage.builder().chatId(who.toString()).text(what).build();
        message.enableHtml(true);
        try{
            execute(message);
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }
    
    public void sendWelcomeText(Long who,String what){
        SendMessage sm = SendMessage.builder().parseMode("HTML")
        .chatId(who.toString()).text(what).build();
        sm.enableHtml(true);
        
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        InlineKeyboardButton ruleBtn = InlineKeyboardButton.builder().text("     See Rules and Regulations.     ").build();
        ruleBtn.setUrl("https://t.me/ByteBoi_bot?start=rules");
        // Set the keyboard to the markup
        rowInline.add(ruleBtn);
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        sm.setReplyMarkup(markupInline);
        try{
            execute(sm);
             
        }catch (TelegramApiException e){
            throw new RuntimeException(e);
        }
    }
}
