package IslandFurniture.WAR3.POS;
import java.io.IOException;
import java.util.logging.Logger;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;


@Provider
@PreMatching
public class RESTAPIFILTER implements ContainerResponseFilter,ContainerRequestFilter {
    private final static Logger log = Logger.getLogger( RESTAPIFILTER.class.getName() );
    @Override
    public void filter( ContainerRequestContext requestCtx, ContainerResponseContext responseCtx ) throws IOException {
        log.info( "Filtering REST Response" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Origin", "*" );    // You may further limit certain client IPs with Access-Control-Allow-Origin instead of '*'
        responseCtx.getHeaders().add( "Access-Control-Allow-Credentials", "true" );
        responseCtx.getHeaders().add( "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT" );
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        
    }
}
