package com.FriendsChatMonitor;

import com.google.gson.Gson;
import com.google.inject.Provides;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Slf4j
@PluginDescriptor(
    name = "Friends Chat Monitor",
    description = "Forwards Friends Chat notifications to a Discord SaaS with deduplication",
    tags = {"friends chat", "discord", "loot", "saas"}
)
public class FriendsChatMonitorPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private FriendsChatMonitorConfig config;

    @Inject
    private OkHttpClient okHttpClient;

    @Inject
    private Gson gson;

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
    // Replace with your actual Cloudflare Worker URL when deployed
    private static final String API_ENDPOINT = "https://your-worker-url.workers.dev/ingest";

    @Provides
    FriendsChatMonitorConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(FriendsChatMonitorConfig.class);
    }

    @Subscribe
    public void onChatMessage(ChatMessage event)
    {
        // Filter for Friends Chat notification
        if (event.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION)
        {
            sendToSaaS(event.getSender(), event.getMessage());
        }
    }

    private void sendToSaaS(String author, String content)
    {
        if (config.apiKey().isEmpty())
        {
			log.warn("FriendsChatMonitor: No API key provided");
            return;
        }

        Map<String, String> data = new HashMap<>();
        data.put("author", author);
        data.put("content", content);

        Request request = new Request.Builder()
            .url(API_ENDPOINT)
            .addHeader("X-SaaS-Token", config.apiKey())
            .post(RequestBody.create(JSON, gson.toJson(data)))
            .build();

        // Async execution ensures the game thread isn't blocked by the HTTP request
        okHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                log.warn("FriendsChatMonitor: Failed to send data to SaaS", e);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                response.close();
            }
        });
    }
}