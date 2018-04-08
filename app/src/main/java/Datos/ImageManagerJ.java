package Datos;

/**
 * Created by ramir on 4/8/2018.
 */

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.LinkedList;

public class ImageManagerJ {
    /*
    **Only use Shared Key authentication for testing purposes!**
    Your account name and account key, which give full read/write access to the associated Storage account,
    will be distributed to every person that downloads your app.
    This is **not** a good practice as you risk having your key compromised by untrusted clients.
    Please consult following documents to understand and use Shared Access Signatures instead.
    https://docs.microsoft.com/en-us/rest/api/storageservices/delegating-access-with-a-shared-access-signature
    and https://docs.microsoft.com/en-us/azure/storage/common/storage-dotnet-shared-access-signature-part-1
    */
    public static final String storageConnectionString = "DefaultEndpointsProtocol=https;"
            + "AccountName=findoor;"
            + "AccountKey=CMyTTng6aQLscjgyTf19VK35ovOr1ABW9EVh9A9KY1ulv7GP6/5zGAn5z9tTwYpASphNEHjgkNMICOLk4jTtgw==";

    private static CloudBlobContainer getContainer() throws Exception {
        // Retrieve storage account from connection-string.

        CloudStorageAccount storageAccount = CloudStorageAccount
                .parse(storageConnectionString);

        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to a container.
        // The container name must be lower case
        CloudBlobContainer container = blobClient.getContainerReference("imagenes");

        return container;
    }

    public static String UploadImage(InputStream image, int imageLength, String name) throws Exception {
        CloudBlobContainer container = getContainer();

        container.createIfNotExists();

        CloudBlockBlob imageBlob = container.getBlockBlobReference(name);
        imageBlob.upload(image, imageLength);

        return name;

    }

    public static String[] ListImages() throws Exception{
        CloudBlobContainer container = getContainer();

        Iterable<ListBlobItem> blobs = container.listBlobs();

        LinkedList<String> blobNames = new LinkedList<>();
        for(ListBlobItem blob: blobs) {
            blobNames.add(((CloudBlockBlob) blob).getName());
        }

        return blobNames.toArray(new String[blobNames.size()]);
    }

    public static void GetImage(String name, OutputStream imageStream, long imageLength) throws Exception {
        CloudBlobContainer container = getContainer();

        CloudBlockBlob blob = container.getBlockBlobReference(name);

        if(blob.exists()){
            blob.downloadAttributes();

            imageLength = blob.getProperties().getLength();

            blob.download(imageStream);
        }
    }

    static final String validChars = "abcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

}
