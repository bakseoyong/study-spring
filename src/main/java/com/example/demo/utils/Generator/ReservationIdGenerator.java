package com.example.demo.utils.Generator;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.procedure.ProcedureOutputs;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import javax.persistence.ParameterMode;
import java.io.Serializable;
import java.util.Properties;

public class ReservationIdGenerator implements IdentifierGenerator //Configurable deprecated
{
    public static final String RESERVATION_ID_GENERATOR_KEY = "procedureParam";
    private String procedureParam;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        ProcedureCall procedureCall = session.createStoredProcedureCall("nextval");
        procedureCall.registerParameter("inputParam", String.class, ParameterMode.IN);
        procedureCall.registerParameter("ID", String.class, ParameterMode.OUT);

        procedureCall.setParameter("inputParam", procedureParam);

        ProcedureOutputs outputs = procedureCall.getOutputs();

        return (String) outputs.getOutputParameterValue("ID");
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        this.procedureParam = ConfigurationHelper.getString(RESERVATION_ID_GENERATOR_KEY, params);
    }

}
