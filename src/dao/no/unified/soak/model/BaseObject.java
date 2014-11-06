/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * Base class for Model objects.  Child objects should implement toString(),
 * equals() and hashCode();
 *
 * <p>
 * <a href="BaseObject.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public abstract class BaseObject implements Serializable {
    public abstract String toString();

    public abstract boolean equals(Object o);

    public abstract int hashCode();
}
