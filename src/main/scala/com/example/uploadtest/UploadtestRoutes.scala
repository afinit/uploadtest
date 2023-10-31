package com.example.uploadtest

import cats.effect.Async
import cats.implicits._
import org.http4s.{EntityDecoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.multipart.Multipart

import java.nio.file.Files

class UploadtestRoutes[F[_]: Async: fs2.io.file.Files](
    multipartDecoder: EntityDecoder[F, Multipart[F]],
) {

  private def _downloadFile(stream: fs2.Stream[F, Byte]) = {
    val tmpFile = Files.createTempFile("uploads", ".tmp")
    println(s"TEMP FILE HERE: ${tmpFile}")
    val fs2File = fs2.io.file.Path.fromNioPath(tmpFile)
    stream
      .through(fs2.io.file.Files[F].writeAll(fs2File))
      .compile
      .drain
      .map(_ => Files.delete(tmpFile))
  }

  def routes: HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "dataSource" =>
        req
          .decode[Multipart[F]] { m =>
            val stream = m.parts.find(_.name.contains("file")).map(_.body).get
            println("RECEIVED FILE FOR DOWNLOAD")
            for {
              _ <- _downloadFile(stream)
              _ = println("FILE DOWNLOAD COMPLETE")
              response <- Ok("BOB")
            } yield response

          }(implicitly, multipartDecoder)
          .handleError { e =>
            println(s"EXCEPTION HERE: ${e.getMessage}")
            throw e
          }
    }
  }
}
