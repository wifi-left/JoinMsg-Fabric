package io.wifi.joinmsg.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

// import static net.minecraft.command.argument.ScoreHolderArgumentType;
import static net.minecraft.server.command.CommandManager.argument;

import static net.minecraft.server.command.CommandManager.literal;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import io.wifi.joinmsg.joinmsg;

// import net.minecraft.text.TranslatableText;
public class commandMiraimc {

    // private static final SimpleCommandExceptionType EDIT_TAG_EXCEPTION = new
    // SimpleCommandExceptionType(new
    // TranslatableText("arguments.editfunction.tag.unsupported"));
    // private static final SimpleCommandExceptionType MOD_NOT_INSTALLED_EXCEPTION =
    // new SimpleCommandExceptionType(new
    // TranslatableText("commands.editfunction.failed.modNotInstalled"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("miraimc")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(command -> {
                    command.getSource().sendFeedback(Text.literal("※ Help Infomation: /miraimc help"), false);
                    return 1;
                })
                .then(literal("reload").executes(command -> {
                    joinmsg.reloadConfig();
                    command.getSource().sendFeedback(Text.literal("√ Reload the JOINMSG config successfully."), true);
                    return 1;
                }))
                .then(literal("help").executes(command -> {
                    command.getSource().sendFeedback(Text.literal(
                            "/miraimc reload - Reload the configs from local file.\n/miraimc help - Show the help information of the command.\n/miraimc send <message> - Post the message to the url.\n/miraimc info - Get the Config content."),
                            false);
                    return 1;
                }))
                .then(literal("send")
                        .then(argument("message", StringArgumentType.greedyString()).executes(command -> {
                            String message = StringArgumentType.getString(command,
                                    "message");
                            ServerPlayerEntity player = command.getSource().getPlayer();
                            if (player != null) {
                                message = "<" + player.getName() + ">" + " " + message;
                            } else {
                                message = "[CONSOLES] " + message;
                            }
                            boolean success = joinmsg.sendPostMessage(message);
                            if (success) {
                                command.getSource().sendFeedback(Text.literal("√ [JOINMSG] Send Message Successfully."),
                                        true);
                                System.out.println("Send message to the SERVER and succeeded: " + message);
                                return 1;
                            } else {
                                command.getSource().sendFeedback(Text.literal("× [JOINMSG] Send Message: Failed."),
                                        true);
                                System.out.println("Send message to the SERVER but failed: " + message);
                                return 0;
                            }
                        })))
                .then(literal("info").executes(command -> {
                    command.getSource().sendFeedback(Text.literal(
                            "[Server Start Message] " + joinmsg.serverStart + "\n[Player Join Message]\n - Prefix: "
                                    + joinmsg.joinMsg_pre + "\n - Suffix: " + joinmsg.joinMsg_aft
                                    + "\n[Player Leave Message]\n - Prefix: " + joinmsg.leaveMsg_pre + "\n - Suffix: "
                                    + joinmsg.leaveMsg_aft),
                            false);
                    return 1;
                }))

        );
    }
}
