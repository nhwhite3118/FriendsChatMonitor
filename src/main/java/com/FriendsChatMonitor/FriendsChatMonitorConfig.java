package com.FriendsChatMonitor;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("friendschatmonitor")
public interface FriendsChatMonitorConfig extends Config
{
    @ConfigItem(
        keyName = "enabled",
        name = "Enable Monitoring",
        description = "Toggle the monitoring and forwarding of Friends Chat messages",
        position = 0,
        warning = "This feature submits your IP address to a 3rd-party server not controlled or verified by RuneLite developers"
    )
    default boolean enabled() { return false; }

    @ConfigItem(
        keyName = "apiKey",
        name = "SaaS API Key",
        description = "Your unique API key from the dashboard",
        position = 1,
        secret = true
    )
    default String apiKey() { return ""; }

    @ConfigItem(
        keyName = "friendsChatName",
        name = "Friends Chat Name",
        description = "The username of the friends chat owner to monitor",
        position = 2
    )
    default String friendsChatName() { return ""; }
}