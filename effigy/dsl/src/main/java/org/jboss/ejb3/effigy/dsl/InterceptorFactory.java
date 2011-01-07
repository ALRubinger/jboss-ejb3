/*
 * JBoss, Home of Professional Open Source.
 * Copyright (c) 2011, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ejb3.effigy.dsl;

import org.jboss.ejb3.effigy.InterceptorEffigy;

import javax.interceptor.InvocationContext;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class InterceptorFactory
{
   private InterceptorEffigyImpl interceptorEffigy;

   InterceptorFactory()
   {
      this.interceptorEffigy = new InterceptorEffigyImpl();
   }

   public InterceptorFactory aroundInvoke(String methodName) throws NoSuchMethodException
   {
      interceptorEffigy.addAroundInvoke(method(methodName));
      return this;
   }

   public InterceptorEffigy effigy()
   {
      return interceptorEffigy;
   }
   
   public static InterceptorFactory interceptor(Class<?> interceptorClass)
   {
      InterceptorFactory factory = new InterceptorFactory();
      factory.interceptorClass(interceptorClass);
      return factory;
   }

   public InterceptorFactory interceptorClass(Class<?> cls)
   {
      interceptorEffigy.setInterceptorClass(cls);
      return this;
   }

   private Method method(String methodName) throws NoSuchMethodException
   {
      Class<?> interceptorClass = interceptorEffigy.getInterceptorClass();
      Method method;
      try
      {
         method = interceptorClass.getMethod(methodName, InvocationContext.class);
      }
      catch(NoSuchMethodException e)
      {
         method = interceptorClass.getDeclaredMethod(methodName, InvocationContext.class);
         method.setAccessible(true);
      }
      return method;
   }

   public InterceptorFactory postConstruct(String methodName) throws NoSuchMethodException
   {
      interceptorEffigy.addPostConstruct(method(methodName));
      return this;
   }
}
