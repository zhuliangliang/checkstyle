////////////////////////////////////////////////////////////////////////////////
// checkstyle: Checks Java source code for adherence to a set of rules.
// Copyright (C) 2001-2003  Oliver Burn
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
////////////////////////////////////////////////////////////////////////////////
package com.puppycrawl.tools.checkstyle.checks.j2ee;

import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

/**
 * Checks that a ejbCreate method is void. Useful for checking SessionBeans.
 * @author Rick Giles
 */
public class NonVoidEjbCreateCheck
    extends AbstractBeanCheck
{
    /**
     * @see com.puppycrawl.tools.checkstyle.api.Check
     */
    public int[] getDefaultTokens()
    {
        return new int[] {TokenTypes.METHOD_DEF};
    }

    /**
     * @see com.puppycrawl.tools.checkstyle.api.Check
     */
    public int[] getRequiredTokens()
    {
        return getDefaultTokens();
    }

    /**
     * @see com.puppycrawl.tools.checkstyle.api.Check
     */
    public void visitToken(DetailAST aAST)
    {
        DetailAST definer = aAST.getParent();
        while ((definer != null)) {
            if (definer.getType() == TokenTypes.CLASS_DEF) {
                break;
            }
            definer = definer.getParent();
        }
        if (definer != null) {
            if (Utils.hasImplements(definer, "javax.ejb.SessionBean")) {
                final DetailAST nameAST = aAST.findFirstToken(TokenTypes.IDENT);
                if (Utils.isPublicMethod(aAST, "ejbCreate", false, 1)) {
                    log(nameAST.getLineNo(), nameAST.getColumnNo(),
                        "nonvoidejbcreate.sessionbean");
                }
            }
        }
    }
}