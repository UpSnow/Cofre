@echo off
echo === Compilando Cofre Digital (Monolito Modular Refatorado) ===
if not exist out mkdir out

javac -encoding UTF-8 -d out ^
  src\cofre\compartilhado\Constantes.java ^
  src\cofre\compartilhado\Pedido.java ^
  src\cofre\compartilhado\Resposta.java ^
  src\cofre\servidor\model\CofreModel.java ^
  src\cofre\servidor\model\ResultadoJogada.java ^
  src\cofre\servidor\service\JogoService.java ^
  src\cofre\servidor\network\ClienteHandler.java ^
  src\cofre\servidor\network\ServidorTCP.java ^
  src\cofre\servidor\theme\TemaServidor.java ^
  src\cofre\servidor\components\StatCard.java ^
  src\cofre\servidor\components\LogPanel.java ^
  src\cofre\servidor\view\ServidorView.java ^
  src\cofre\servidor\MainServidor.java ^
  src\cofre\cliente\model\JogadaResult.java ^
  src\cofre\cliente\network\ConexaoServidor.java ^
  src\cofre\cliente\service\ClienteService.java ^
  src\cofre\cliente\theme\TemaCliente.java ^
  src\cofre\cliente\components\MiniStatCard.java ^
  src\cofre\cliente\components\HistoricoPanel.java ^
  src\cofre\cliente\components\FormularioPanel.java ^
  src\cofre\cliente\view\ClienteView.java ^
  src\cofre\cliente\MainCliente.java

if %errorlevel%==0 (
    echo.
    echo Compilacao OK!
    echo.
    echo  Servidor:  java -cp out cofre.servidor.MainServidor
    echo  Cliente:   java -cp out cofre.cliente.MainCliente
    echo.
    echo Abra o servidor primeiro, depois quantos clientes quiser.
) else (
    echo ERRO na compilacao.
)
pause
