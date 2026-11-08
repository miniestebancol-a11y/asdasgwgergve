package com.ejemplo.boton4.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    // Indica que el botón 4 ya se presionó y el inventario puede abrirse
    private static boolean boton4Presionado = false;

    @WrapOperation(
        method = "handleKeybinds",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;consumeClick()Z")
    )
    private boolean boton4AntesDelInventario(KeyMapping tecla, Operation<Boolean> original) {
        Minecraft mc = (Minecraft) (Object) this;

        if (tecla != mc.options.keyInventory) {
            return original.call(tecla);
        }

        // Segundo paso: el botón 4 ya se presionó, ahora sí se abre el inventario
        if (boton4Presionado) {
            boton4Presionado = false;
            return true;
        }

        boolean apretado = original.call(tecla);
        if (apretado) {
            // Primer paso: se presiona el botón 4 y se posterga la apertura un tick
            KeyMapping.click(InputConstants.Type.MOUSE.getOrCreate(GLFW.GLFW_MOUSE_BUTTON_4));
            boton4Presionado = true;
            return false;
        }
        return false;
    }
}
