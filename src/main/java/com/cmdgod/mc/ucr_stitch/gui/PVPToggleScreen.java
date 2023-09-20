package com.cmdgod.mc.ucr_stitch.gui;

import javax.swing.text.TextAction;

import com.cmdgod.mc.ucr_stitch.tools.Utility;

import eu.pb4.sgui.api.elements.BookElementBuilder;
import eu.pb4.sgui.api.gui.BookGui;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PVPToggleScreen extends BookGui {

    private static final String ENABLE_PVP_CMD = "/enablepvp";
    private static final String DISABLE_PVP_CMD = "/disablepvp";
    private static final String CANCEL_CMD = "/cancel";

    public PVPToggleScreen(ServerPlayerEntity player, boolean turnOn) {
        super(player, turnOn ? constructEnablePVPBook() : constructDisablePVPBook());
    }

    private static BookElementBuilder constructEnablePVPBook() {
        BookElementBuilder book = new BookElementBuilder();
        book.addPage(
                Text.of("      ENABLE PVP").getWithStyle(Style.EMPTY.withColor(Formatting.GREEN)).get(0),
                Text.of(""),
                Text.of("By enabling PVP, you can fight other players, get 50% more XP and deal 15% more damage against non-players, but you also become vulnerable to other players' attacks."),
                Text.of("       Enable PVP?"),
                Text.of(""),
                getYesNoText(ENABLE_PVP_CMD)
        );
        return book;
    }

    private static BookElementBuilder constructDisablePVPBook() {
        BookElementBuilder book = new BookElementBuilder();
        book.addPage(
                Text.of("      DISABLE PVP").getWithStyle(Style.EMPTY.withColor(Formatting.GREEN)).get(0),
                Text.of(""),
                Text.of("By disabling PVP, you cannot fight other players and lose out on War Mode bonuses, but you are safe from other players' attacks.")
        );
        book.addPage(  
                Text.of(""),
                Text.of("        WARNING!").getWithStyle(Style.EMPTY.withColor(Formatting.DARK_RED)).get(0),
                Text.of("Disabling PVP mode requires a minute of being stationary and being unable to do anything. Also, any damage you take cancels the ritual!").getWithStyle(Style.EMPTY.withColor(Formatting.RED)).get(0),
                Text.of("   Proceed anyway?"),
                Text.of(""),
                getYesNoText(DISABLE_PVP_CMD)
        );
        return book;
    }

    private static Text getYesNoText(String yesCommand) {
        return Text.Serializer.fromJson("[{\"text\":\"   [ NO ]\",\"color\":\"dark_red\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[{\"text\":\"Cancel and Close\",\"color\":\"red\"}]},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + CANCEL_CMD + "\"}},{\"text\":\"      \", \"clickEvent\": {}, \"hoverEvent\": {}},{\"text\":\"[ YES ]\",\"color\":\"dark_green\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":[{\"text\":\"Accept!\",\"color\":\"green\"}]},\"clickEvent\":{\"action\":\"run_command\",\"value\":\"" + yesCommand + "\"}}]");
    }

    @Override
    public boolean onCommand(String command) {
        if (command.equals(ENABLE_PVP_CMD)) {
            Utility.getInterfacePlayer(player).enablePVP();
            return true;
        }
        if (command.equals(DISABLE_PVP_CMD)) {
            Utility.getInterfacePlayer(player).startDisablingPVP();
            return true;
        }
        if (command.equals(CANCEL_CMD)) {
            this.close();
            return true;
        }
        return false;
    }

}
