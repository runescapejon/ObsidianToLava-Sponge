package me.runescapejon.obsidian;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.Hotbar;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "obsidiantolava", name = "ObsidianToLava-Sponge", authors = {
		"runescapejon" }, description = "Right Click Obsidian with an empty bucket to get a Lava Bucket if they have permission!", version = "1.2")
public class Obsidian {

	private ConfigurationNode Config;
	private File DConfig;
	private ConfigurationLoader<CommentedConfigurationNode> ConfigNode;
	public static Obsidian instance;

	public ConfigurationLoader<CommentedConfigurationNode> getConfig() {
		return this.ConfigNode;
	}

	@Inject
	public Obsidian(@DefaultConfig(sharedRoot = false) File DConfig,
			@DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> Configs) {
		this.DConfig = DConfig;
		this.ConfigNode = Configs;
		instance = this;
	}

	@Listener
	public void onPreInit(GamePreInitializationEvent event) throws IOException {
		try {
			if (!this.DConfig.exists()) {
				this.DConfig.createNewFile();

				this.Config = getConfig().load();
				this.Config.getNode("Languages", "InventoryIsFull")
						.setValue("Inventory is full please make a place to convert Obsidian to lava.");
				this.Config.getNode("Languages", "ConvertedObsidianToLava")
						.setValue("&6Just Converted Obsidian to lava bucket check your inventory for the lava bucket.");
				getConfig().save(this.Config);
			}
			this.Config = getConfig().load();
			Language.SetInventorySpacelMsg(TextSerializers.FORMATTING_CODE
					.deserialize(this.Config.getNode("Languages", "InventoryIsFull").getString()));
			Language.SetInventoryMsg(TextSerializers.FORMATTING_CODE
					.deserialize(this.Config.getNode("Languages", "ConvertedObsidianToLava").getString()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Listener
	public void ObsidianToLava(InteractBlockEvent.Secondary.MainHand event, @First Player p) {
		Text msg = Text.builder().append(Language.getInventoryMsg()).build();
		Text msg2 = Text.builder().append(Language.getInventorySpaceMsg()).build();
		if (p.hasPermission("obsidian.lava")) {
			if (p.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
				ItemStack stack = p.getItemInHand(HandTypes.MAIN_HAND).get();

				if (stack.getType().equals(ItemTypes.BUCKET)) {
					//need isPresent() because i am getting No value present error
					if (event.getTargetBlock().getLocation().isPresent()) {
						Location<World> block = event.getTargetBlock().getLocation().get();
						if (block.getBlock().getType() == BlockTypes.OBSIDIAN) {
							if (p.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(GridInventory.class),
									QueryOperationTypes.INVENTORY_TYPE.of(Hotbar.class)).size() == 36) {
								p.sendMessage(Text.of(msg2));
								return;
							}
							int quantity = stack.getQuantity();
							stack.setQuantity(quantity - 1);
							p.setItemInHand(HandTypes.MAIN_HAND, stack);
							p.sendMessage(Text.of(msg));
							block.setBlockType(BlockTypes.AIR);
							p.getInventory().offer(ItemStack.of(ItemTypes.LAVA_BUCKET, 1));

						}
					}

				}
			}
		}
	}
}