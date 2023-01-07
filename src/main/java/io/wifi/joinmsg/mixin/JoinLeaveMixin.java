package io.wifi.joinmsg.mixin;
import io.wifi.joinmsg.joinmsg;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class JoinLeaveMixin {

    @Inject(at = @At("HEAD"), method = "broadcast(Lnet/minecraft/text/Text;Z)V", cancellable = true)
    void filterBroadCastMessages(Text message, boolean overlay, CallbackInfo ci) {

        String Key = ((TranslatableTextContent) message.getContent()).getKey();
        if (Key.equals("multiplayer.player.joined.renamed")) {
            String player = ((TranslatableTextContent) message.getContent()).getArg(0).getString();
            joinmsg.sendPostMessage(joinmsg.joinMsg_pre+player+joinmsg.joinMsg_aft);
        } else if(Key.equals("multiplayer.player.joined")){
            String player = ((TranslatableTextContent) message.getContent()).getArg(0).getString();
            joinmsg.sendPostMessage(joinmsg.joinMsg_pre+player+joinmsg.joinMsg_aft);
        } else if(Key.equals("multiplayer.player.left")){
            String player = ((TranslatableTextContent) message.getContent()).getArg(0).getString();
            joinmsg.sendPostMessage(joinmsg.leaveMsg_pre+player+joinmsg.leaveMsg_aft);
        }
    }
}