module ControllerModule
  def setdefaults(user)
	h1 =  {'lcase' => 'Regular Case', 'tfidf' => 'Idf', 'algo' => 'Combined', 'tolerance' => 50,'iterations' => 5, 'maxwords' => 15, 'color' => 1, 'angle' => 1, 'scale' => 1, 'font' => 'ChunkFive'}
	user.settings.lcase = h1['lcase']
	user.settings.tfidf = h1['tfidf']
	user.settings.algo = h1['algo']
	user.settings.tolerance = h1['tolerance']
	user.settings.iterations = h1['iterations'] 
	user.settings.maxwords = h1['maxwords']
	user.settings.color = h1['color']
	user.settings.angle = h1['angle']
	user.settings.scale = h1['scale']
	user.settings.font = h1['font']
  end
  def resetdefault(user, key)
	h1 =  {'lcase' => 'Regular Case', 'tfidf' => 'Idf', 'algo' => 'Combined', 'tolerance' => 50, 'iterations' => 5, 'maxwords' => 15, 'color' => 1, 'angle' => 1, 'scale' => 1}
	if key == 'lcase'
		user.settings.lcase = h1['lcase']
	elsif key == 'tfidf'
		user.settings.tfidf = h1['tfidf']
	elsif key == 'algo'
		user.settings.algo = h1['algo']
	elsif key == 'tolerance'
		user.settings.tolerance = h1['tolerance']
	elsif key == 'iterations'
		user.settings.iterations = h1['iterations']
	elsif key == 'maxwords'
		user.settings.maxwords = h1['maxwords']
	elsif key == 'color'
		user.settings.color = h1['color']
	elsif key == 'angle'
		user.settings.angle = h1['angle']
	elsif key == 'scale'
		user.settings.scale = h1['scale']
	elsif key == 'font'
		user.settings.font = h1['font']
	end
  end
end