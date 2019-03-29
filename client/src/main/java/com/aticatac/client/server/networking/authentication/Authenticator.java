package com.aticatac.client.server.networking.authentication;

import com.aticatac.client.server.networking.Client;
import com.aticatac.client.server.networking.Server;
import com.aticatac.client.server.networking.listen.CommandListener;
import com.aticatac.common.model.ClientModel;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.Login;
import com.aticatac.common.model.ModelReader;
import com.aticatac.common.model.Updates.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import org.apache.log4j.Logger;

/**
 * The type Authenticator.
 */
public class Authenticator implements Runnable {
  private final Logger logger;
  private final PrintStream printer;
  private final BufferedReader reader;
  private final ModelReader modelReader;

  /**
   * Instantiates a new Authenticator.
   *
   * @param client the client
   * @throws IOException the io exception
   */
  public Authenticator(Socket client) throws IOException {
    this.logger = Logger.getLogger(getClass());
    this.printer = new PrintStream(client.getOutputStream());
    this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    this.modelReader = new ModelReader();
  }

  @Override
  public void run() {
    try {
      Login login = getRequest();
      if (clientExists(login)) {
        this.logger.warn("Client already exists... Rejecting...");
        reject(login, Response.TAKEN);
      } else if (login.getId().equals("")) {
        reject(login, Response.INVALID_NAME);
      } else if (Server.ServerData.INSTANCE.playerCount() >= Server.ServerData.INSTANCE.getMaxPlayers() - 1) {
        reject(login, Response.FULL);
      } else {
        accept(login);
      }
    } catch (IOException | InvalidBytes e) {
      this.logger.error(e);
    }
    this.logger.info("Finished!");
  }

  /**
   * @return Login request
   * @throws IOException  IO exception
   * @throws InvalidBytes InvalidByte exception
   */
  private Login getRequest() throws IOException, InvalidBytes {
    this.logger.trace("Expecting " + Login.class.getCanonicalName() + " from socket.");
    String json = this.reader.readLine();
    this.logger.trace("Client sent " + json + " bytes.");
    return modelReader.fromJson(json, Login.class);
  }

  private boolean clientExists(Login clientModel) {
    //TODO add additional authenticationOLD for existing users.
    // also this is where security layers could be added.
    return Server.ServerData.INSTANCE.getClients().containsKey(clientModel.getId());
  }

  /**
   * Reject client.
   *
   * @param login login details
   */
  private void reject(Login login, Response reason) {
    this.logger.trace("Rejecting client...");
    login.setAuthenticated(reason);
    this.printer.println(modelReader.toJson(login));
  }

  /**
   * Accepts the client and add them to the client map.
   *
   * @param login login details
   */
  private void accept(Login login) {
    this.logger.trace("Accepting client...");
    login.setAuthenticated(Response.ACCEPTED);
    //TODO add correct map id
    login.setMapID(1);
    login.setMulticast(Server.ServerData.INSTANCE.getMulticast().getHostAddress());
    this.logger.trace("Setting multicast address: " + login.getMulticast());
    this.printer.println(modelReader.toJson(login));
    addClient(login);
    this.logger.info("Client: " + login.getId() + " accepted.");
  }

  /**
   * Add client to client map.
   *
   * @param login login details
   */
  private void addClient(Login login) {
    CommandListener listener = new CommandListener(this.reader,login);
    ClientModel model = new ClientModel(login.getId());
    Client client = new Client(listener, model, this.printer);
    Server.ServerData.INSTANCE.getGame().addPlayer(client.getId());
    Server.ServerData.INSTANCE.getClients().put(model.getId(), client);
  }
}
