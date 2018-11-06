package si.cnr.it.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Utenza.class)
public abstract class Utenza_ {

	public static volatile SingularAttribute<Utenza, User> user_utenza;
	public static volatile SingularAttribute<Utenza, Istituto> istituto_user;
	public static volatile SingularAttribute<Utenza, Long> id;
	public static volatile SingularAttribute<Utenza, String> matricola;

}

