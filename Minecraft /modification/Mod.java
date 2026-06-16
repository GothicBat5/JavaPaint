package com.example.mymod;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class Mod implements ModInitializer 
{

    public static final String MOD_ID = "my_mod";

    public static final Item RUBY = new Item(new Item.Settings());

    @Override
    public void onInitialize() 
    {

        Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "ruby"), RUBY);

        System.out.println("Ruby item registered!");
    }
}
