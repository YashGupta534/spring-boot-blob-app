package com.example.demo;

/**
 * Azure Blob Storage quickstart
 */
import com.azure.identity.*;
import com.azure.storage.blob.*;
import com.azure.storage.blob.models.*;
import java.io.*;
import java.util.Locale;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.ListBlobContainersOptions;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.StorageSharedKeyCredential;

@RestController
public class HelloController {
	
	@GetMapping("/")
	public String hello() {
                System.out.println( "\nHome Page" );
        return "welcome";
	}
	
	@GetMapping("/test")
	public String test() 
        {
                String accountName = "<account name>";
                String accountKey = "<access key>";
                StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
                String endpoint = String.format(Locale.ROOT, "https://%s.blob.core.windows.net", accountName);
                BlobServiceClient storageClient = new BlobServiceClientBuilder().endpoint(endpoint).credential(credential)
                        .buildClient();
                BlobContainerClient blobContainerClient = storageClient.getBlobContainerClient("<blob container name>");
                blobContainerClient.createIfNotExists();
                BlockBlobClient blockBlobClient = blobContainerClient.getBlobClient("UTC=2022-12-22/2022-12-22.csv")
                        .getBlockBlobClient();
                String data = "Hello ,world3, world4, world5";
                try (ByteArrayInputStream dataStream = new ByteArrayInputStream(data.getBytes())) {
                blockBlobClient.upload(dataStream, data.length(), true);
                System.out.println("Completed");
                } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
                }
                
                return "Completed";
	}

        @GetMapping("/storage")
        public String Storage()
        {
                System.out.println( "\nInitializing defaultCredential" );
                /*
                * The default credential first checks environment variables for configuration
                * If environment configuration is incomplete, it will try managed identity
                */
                DefaultAzureCredential defaultCredential = new DefaultAzureCredentialBuilder().build();
                System.out.println( "\nInitializing blobServiceClient" );
                // Azure SDK client builders accept the credential as a parameter
                // TODO: Replace <storage-account-name> with your actual storage account name
                BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                        .endpoint("https://<storage-account>.blob.core.windows.net/")
                        .credential(defaultCredential)
                        .buildClient();
        
                // Create a unique name for the container
                //String containerName = "quickstartblobs" + java.util.UUID.randomUUID();
                String containerName = "quickstartblobs6802394c-2536-4568-bc6c-ad243604181d";
                
                // Create the container and return a container client object
                BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
                
                ListBlobContainersOptions options = new ListBlobContainersOptions();

                System.out.println("\nList containers:");
                for (BlobContainerItem blobContainerItem : blobServiceClient.listBlobContainers(options, null)) {
                System.out.printf("Container name: %s%n", blobContainerItem.getName());
                }

                System.out.println("\nListing blobs...");

                //List the blob(s) in the container.
                for (BlobItem blobItem : blobContainerClient.listBlobs()) {
                System.out.println("\t" + blobItem.getName());
                }

                return "Completed";
        }
	
}
