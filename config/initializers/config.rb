require 'ostruct'

def load_config_yaml(config_file)
   YAML.load(File.read(Rails.root.join('config', config_file)))[Rails.env]
end

Rails.configuration.paperclip = load_config_yaml('paperclip.yml')
Rails.configuration.access_key_id = 'AKIAJWNZNLHF7XRD7G5A'
Rails.configuration.secret_access_key = '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'
Rails.configuration.version = 1


