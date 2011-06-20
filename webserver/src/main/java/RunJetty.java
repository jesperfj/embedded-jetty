import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppContext;

public class RunJetty
{
    public static void main(String[] args) throws Exception
    {
        Server server = new Server();
        String dburl = System.getenv("DATABASE_URL");
        System.setProperty("database.url", 
                           dburl.replaceAll("postgres://(.*):(.*)@(.*)","jdbc:postgresql://$3?user=$1&password=$2"));
        if(dburl.startsWith("postgres")) {
            System.setProperty("hibernate.dialect","org.hibernate.dialect.PostgreSQLDialect");
        }
        Connector connector = new SelectChannelConnector();
        connector.setPort(Integer.getInteger("jetty.port",8080).intValue());
        server.setConnectors(new Connector[] { connector });
	if(args.length<1) {
            System.out.println("Missing required argument: path_to_war_file");
            System.exit(1);
        }

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setWar(args[0]);

        server.setHandler(webapp);

        server.start();
        server.join();
    }
}
