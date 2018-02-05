package me.runescapejon.obsidian;

import org.spongepowered.api.text.Text;

public class Language {
	private static Text invspacemsg = Text.builder()
			.append(Text.of("Your inventory is full please make space to convert Obsidian to lava")).build();

	private static Text invmsg = Text.builder()
			.append(Text.of("Inventory is full please make a place to convert Obsidian to lava")).build();

	public static void SetInventoryMsg(Text textmsg) {
		invmsg = textmsg;
	}

	public static Text getInventoryMsg() {
		return invmsg;
	}

	public static void SetInventorySpacelMsg(Text textmsg) {
		invspacemsg = textmsg;
	}

	public static Text getInventorySpaceMsg() {
		return invspacemsg;
	}
}
