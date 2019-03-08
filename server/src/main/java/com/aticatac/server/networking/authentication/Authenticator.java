package com.aticatac.server.networking.authentication;

import com.aticatac.common.model.ClientModel;
import com.aticatac.common.model.Exception.InvalidBytes;
import com.aticatac.common.model.Login;
import com.aticatac.common.model.ModelReader;
import com.aticatac.server.gamemanager.Manager;
import com.aticatac.server.networking.Client;
import com.aticatac.server.networking.Server;
import com.aticatac.server.networking.listen.CommandListener;
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
  private boolean authenticated;

  public Authenticator(Socket client) throws IOException {
    this.logger = Logger.getLogger(getClass());
    this.authenticated = false;
    this.printer = new PrintStream(client.getOutputStream());
    this.reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
  }

  @Override
  public void run() {
    int counter = 0;
    while (!this.authenticated) {
      if (counter++ > 10) {
        break;
      }
      try {
        Login login = getRequest();
        if (clientExists(login)) {
          this.logger.warn("Client already exists... Rejecting...");
          reject(login);
        } else {
          accept(login);
          this.authenticated = true;
        }
      } catch (IOException e) {
        this.logger.error(e);
      } catch (InvalidBytes e) {
        this.logger.error(e);
        break;
      }
    }
    this.logger.warn("Stopping...");
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
    return ModelReader.fromJson(json, Login.class);
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
  private void reject(Login login) {
    this.logger.trace("Rejecting client...");
    login.setAuthenticated(false);
    this.printer.println(ModelReader.toJson(login));
  }

  /**
   * Accepts the client and add them to the client map.
   *
   * @param login login details
   */
  private void accept(Login login) {
    this.logger.trace("Accepting client...");
    login.setAuthenticated(true);
    //TODO add correct map id
    login.setMapID(1);
    login.setMulticast(Server.ServerData.INSTANCE.getMulticast().getHostAddress());
    this.logger.trace("Setting multicast address: " + login.getMulticast());
    this.printer.println(ModelReader.toJson(login));
    addClient(login);
    this.logger.info("Client: " + login.getId() + " accepted.");
  }

  /**
   * Add client to client map.
   *
   * @param login login details
   */
  private void addClient(Login login) {
    CommandListener listener = new CommandListener(this.reader);
    ClientModel model = new ClientModel(login.getId());
    Client client = new Client(listener, model);
    Manager.INSTANCE.addClient(client.getId());
    Server.ServerData.INSTANCE.getClients().put(model.getId(), client);
  }
}
