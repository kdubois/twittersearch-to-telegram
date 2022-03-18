# Demo: Send Tweets to Telegram using Camel Quarkus 

This is a really simple example of how you can use Camel Quarkus to integrate between Twitter and Telegram, but the source or destination can easily be swapped out for something else thanks to Camel's big collection of out-of-the-box components.

This demo was featured on the Openshift Coffee Break episode on March 16 2022: https://www.youtube.com/watch?v=aaEV19c6GhQ

To run this demo as is, you will need to create a Twitter developer account and create an app to generate the needed api keys/secrets (https://developer.twitter.com/en/apply-for-access) and you will also need to create a Telegram bot and add it to a channel/group (https://core.telegram.org/bots).

Once you have them, you can add the values to the application.properties placeholders (be careful not to git commit this file with the values! :D ); or you can just set them as environment variables or whatever your favorite method is.  

And after that, tag me (@kevindubois) in a tweet, fire up Quarkus in dev mode with `./mvnw quarkus:dev` and you should see your tweet arriving in your telegram channel.    (you can change the search parameter to whatever you want, either change the default value in TelegramRoute.java or set an environment variable 'searchterm')

## Deploying to Kubernetes

Everything is in place to deploy this app to Kubernetes, all you have to do is create telegram and twitter secrets (you can find examples in the k8s folder) and build/push the application.  You may need to allow your app to access the secrets as well.  (more about this below)

### JVM hotspot

  By default the app is packaged as an uber-jar that can be easily built/deployed as a 'classic' Java app. (`mvn package`).  In Openshift for example, you can just use the `oc new-app` command like in the example below (provided you checked out the repository with https, otherwise you'll need to create a git secret first)

`oc new-app .`

### Native build

If you want to deploy the app as a Quarkus native build (faster startup, less resource consumption), you will need to have podman installed or docker running, and then the app will be built in a Mandrel container (see quarkus.native.container-build) by invoking the below command.  (note - if you have GraalVM or Mandrel installed locally that's an option as well, just add -Dquarkus.native.container-build=false)

`./mvnw package -Pnative`

Next, you will need to build the container, eg.

`docker build -t twitter-telegram -f src/main/container/Containerfile.native-micro .`

Finally, push it to your Kubernetes cluster.  If you have enabled knative (eg. Openshift Serverless) you can do this using the kn cli tool:

`kn service create twitter-telegram --image=quay.io/kevindubois/twitter-telegram`

### Secrets

You will probably run into an error when you deploy the application saying it cannot get the twitter and telegram secrets.  (if you don't, your cluster may have questionable security policies ;) ).  

If you haven't added the secrets yet, there are examples in the k8s folder, just copy the files, add your credentials and add them to the namespace, eg. 

`oc apply -f k8s/twittersecrets.yaml -f k8s/telegramsecrets.yaml`

Now, to allow access to the secrets you need to create Role & RoleBinding.  Ideally you'd also use a specific service account.  Or you can just go with my example that uses the default serviceaccount and apply the rbac.yaml you can find in the k8s folder as well:

`oc apply -f k8s/rbac.yaml`

Let me know what you think!  Maybe by sending me a tweet @kevindubois and seeing it appear in your Telegram channel :P




