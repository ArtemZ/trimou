package org.trimou.cdi.resolver;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named("alpha")
@RequestScoped
public class Alpha extends BeanWithId {

}
