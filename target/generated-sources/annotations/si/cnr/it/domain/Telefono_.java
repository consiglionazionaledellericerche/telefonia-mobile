package si.cnr.it.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Telefono.class)
public abstract class Telefono_ {

	public static volatile SingularAttribute<Telefono, LocalDate> dataattivazione;
	public static volatile SingularAttribute<Telefono, String> numerocontratto;
	public static volatile SingularAttribute<Telefono, String> numero;
	public static volatile SingularAttribute<Telefono, Istituto> istituto_telefono;
	public static volatile SingularAttribute<Telefono, LocalDate> datacessazione;
	public static volatile SingularAttribute<Telefono, Utenza> utenza_telefono;
	public static volatile SingularAttribute<Telefono, Long> id;
	public static volatile SingularAttribute<Telefono, String> intestatariocontratto;

}

