package com.aizistral.nochatrestrictions.mixin;

import com.aizistral.nochatrestrictions.Constants;
import com.aizistral.nochatrestrictions.core.NCRCore;
import com.aizistral.nochatrestrictions.core.WrappedUserApiService;
import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        Constants.LOG.info("This line is printed by the No Chat Restrictions common mixin!");
        Constants.LOG.info("MC Version: {}", Minecraft.getInstance().getVersionType());
    }

    @Inject(method = "createUserApiService", at = @At("RETURN"), cancellable = true)
    public void onCreateUserApi(YggdrasilAuthenticationService authService, GameConfig gameConfig, CallbackInfoReturnable<UserApiService> info) {
        UserApiService returnedService = info.getReturnValue();
        assert returnedService != null;
        info.setReturnValue(new WrappedUserApiService(returnedService));

        NCRCore.LOGGER.info("Successfully supplanted UserApiService with a wrapped version.");
    }

    @Inject(method = "isNameBanned", at = @At("HEAD"), cancellable = true)
    public void onCheckNameBan(CallbackInfoReturnable<Boolean> info) {
        info.setReturnValue(Boolean.FALSE);
    }

}
