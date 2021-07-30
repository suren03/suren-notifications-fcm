package com.suren.notifications.fcm.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


/**
 * This class initializes the required Mongo configs.
 * Creates MongoClient & MongoDatabase Beans
 *
 * @author Surendra Kumar S
 */

@Slf4j
@Configuration
public class MongoConfig {


    @Value("${aws.db.mongodb.uri}")
    private String connectionUri;

    @Value("${aws.db.mongodb.ssl.truststore.location}")
    private String truststore;

    @Value("${aws.db.mongodb.ssl.truststore.password}")
    private String truststorePassword;

    @Value(("${aws.db.mongodb.database}"))
    private String databaseName;

    @Value(("${aws.db.mongodb.timeout:5}"))
    private int timeout;

    /**
     * Initializes the Mongo client.
     *
     * @return MongoClient
     */
    @Bean
    public MongoClient getMongoClient() {

//        System.setProperty("javax.net.ssl.trustStore", "rds.jks");
//        System.setProperty("javax.net.ssl.trustStorePassword","s3cret");

        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);

        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionUri))
                .applyToSslSettings(builder -> {
                    builder.enabled(true);
                    try {
                        builder.context(getContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                })
                .applyToSocketSettings(builder -> {
                    builder
                            .connectTimeout(timeout, TimeUnit.SECONDS)
                            .readTimeout(timeout, TimeUnit.SECONDS);
                })
                .codecRegistry(codecRegistry)
                .build());
    }


    private SSLContext getContext() throws Exception {

        try {
            KeyStore trustStore = KeyStore.getInstance("JKS");
            char[] trustStorePassword = truststorePassword.toCharArray();
            trustStore.load(new FileInputStream(truststore), trustStorePassword);

            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init(trustStore);

            TrustManager[] managers = factory.getTrustManagers();

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, managers, null);
            return context;
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | KeyManagementException ex) {
            ex.printStackTrace();
            throw (ex);
        }
    }

    /**
     * Creates the instance of MongoDatabase with MongoClient.
     *
     * @return MongoDatabase instance
     */
    @Bean
    public MongoDatabase getMongoDB() {
        return getMongoClient().getDatabase(databaseName);
    }


}
