package net.F53.AtmosphericSoundAdditions.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.events.Event;


public class SoundAtmospherics implements ClientModInitializer{
	public static final Logger LOGGER = LoggerFactory.getLogger("soundatmospherics");

	// Contains the sounds to play at the times
	// I know ew hardcoded not json, ew 2 separate lists for this, not a 2d array
	private static final long[] triggerTimes = new long[]{ 0, 6000, 13000};
	private static final Identifier[] triggerIdentifiers = new Identifier[]{ new Identifier("soundatmospherics:load_in"), new Identifier("soundatmospherics:noon"), new Identifier("soundatmospherics:night_fall")};
	private static SoundEvent[] soundTriggerEvents = new SoundEvent[3];

	private static int soundIndex;

	@Override
	public void onInitializeClient() {
		// initialize sounds
		for (int i = 0; i<triggerIdentifiers.length; i++) {
			soundTriggerEvents[i] = new SoundEvent(triggerIdentifiers[i]);
			Registry.register(Registry.SOUND_EVENT, triggerIdentifiers[i], soundTriggerEvents[i]);
		}

		ClientTickEvents.END_CLIENT_TICK.register((client)->{
			if (client.world != null && client.player != null && client.world.getLevelProperties().getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
				soundIndex = indexOf(client.world.getLunarTime()-1);
				LOGGER.info("soundindex="+soundIndex);
				if (soundIndex != -1) {
					LOGGER.info("attempting to play sound");
					client.world.playSound(client.player.getBlockPos(), soundTriggerEvents[soundIndex], SoundCategory.AMBIENT, 1f, 1f, false);
				}
			}
		});
	}

	public static int indexOf(long key) {
		for (int i = 0; i < triggerTimes.length; i++)
			if (key == triggerTimes[i])
				return i;
		return -1;
	}
}
