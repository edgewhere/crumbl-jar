package io.crumbl.core

import io.crumbl.{BasicUnitSpecs, crypto}
import io.crumbl.models.core.Signer
import io.crumbl.utils.Converter
import scala.io.Source

/**
 * CrumblSpecs test class
 *
 * @author  Cyril Dever
 * @since   1.0
 * @version  1.0
 */
class CrumblSpecs extends BasicUnitSpecs {
  "process()" should "return a crumbled string" in {
    val owner1_pubkey = Converter.hexToBytes("04e315a987bd79b9f49d3a1c8bd1ef5a401a242820d52a3f22505da81dfcd992cc5c6e2ae9bc0754856ca68652516551d46121daa37afc609036ab5754fe7a82a3") // see '../../../resources/keys/owner1.pub'
    val trustee1_pubkey = Converter.hexToBytes("040c96f971c0edf58fe4afbf8735581be05554a8a725eae2b7ad2b1c6fcb7b39ef4e7252ed5b17940a9201c089bf75cb11f97e5c53333a424e4ebcca36065e0bc0") // see '../../../resources/keys/trustee1.pub'
    val trustee2_pubkey = Source.fromResource("keys/trustee2.pub").getLines.mkString("\n")

    val source = "cdever@edgewhere.fr"

    val c = Crumbl(
      source,
      crypto.DEFAULT_HASH_ENGINE,
      Seq(Signer(crypto.ECIES_ALGORITHM, None, Some(Right(owner1_pubkey)))),
      Seq(
        Signer(crypto.ECIES_ALGORITHM, None, Some(Right(trustee1_pubkey))),
        Signer(crypto.RSA_ALGORITHM, None, Some(Left(trustee2_pubkey)))
      )
    )
    val crumbled = c.process
    println(crumbled)
    crumbled.length should be > 0
  }
}
