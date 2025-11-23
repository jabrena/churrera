package info.jab.churrera.cli.config;

import info.jab.churrera.util.CursorApiKeyResolver;
import info.jab.cursor.client.CursorAgentManagement;
import info.jab.cursor.client.CursorAgentInformation;
import info.jab.cursor.client.CursorAgentGeneralEndpoints;
import info.jab.cursor.client.impl.CursorAgentManagementImpl;
import info.jab.cursor.client.impl.CursorAgentInformationImpl;
import info.jab.cursor.client.impl.CursorAgentGeneralEndpointsImpl;
import info.jab.cursor.generated.client.ApiClient;
import info.jab.cursor.generated.client.api.DefaultApi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

/**
 * CDI producer for Cursor API client components.
 */
@ApplicationScoped
public class CursorClientProducer {

    @Inject
    CursorApiKeyResolver apiKeyResolver;

    @Produces
    @Singleton
    public ApiClient produceApiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.updateBaseUri("https://api.cursor.com");
        return apiClient;
    }

    @Produces
    @Singleton
    public DefaultApi produceDefaultApi(ApiClient apiClient) {
        return new DefaultApi(apiClient);
    }

    @Produces
    @Singleton
    public CursorAgentManagement produceCursorAgentManagement(DefaultApi defaultApi) {
        String apiKey = apiKeyResolver.resolveApiKey();
        return new CursorAgentManagementImpl(apiKey, defaultApi);
    }

    @Produces
    @Singleton
    public CursorAgentInformation produceCursorAgentInformation(DefaultApi defaultApi) {
        String apiKey = apiKeyResolver.resolveApiKey();
        return new CursorAgentInformationImpl(apiKey, defaultApi);
    }

    @Produces
    @Singleton
    public CursorAgentGeneralEndpoints produceCursorAgentGeneralEndpoints(DefaultApi defaultApi) {
        String apiKey = apiKeyResolver.resolveApiKey();
        return new CursorAgentGeneralEndpointsImpl(apiKey, defaultApi);
    }
}
