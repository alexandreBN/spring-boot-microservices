# spring-boot-microservices

	# Arquitetura
		Client Side
		^
		| REST
		v
		Router and Gateway Filter   <-Fetches-> Service Discovery
		^					^
		| JWE TOKEN			|
		v					v
		Auth Service		Service (0, ...n-1)
		^					^
		|					|
		v					v
		Database 			Database (0, ...n-1)
	
	Fluxo de Requisições/Respostas:
		1) Cliente manda requisição para o gateway
		2) Gateway manda requisição para o serviço de autenticação
		3) Serviço de Autenticação gera o token
		4) Gateway recebe o token do cliente
		5) Cliente recebe o token
		6) Cliente envia as requisições para o gateway com o token
		7) Gateway recebe e valida o token do cliente